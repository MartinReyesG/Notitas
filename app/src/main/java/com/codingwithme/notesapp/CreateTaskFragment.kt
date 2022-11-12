package com.codingwithme.notesapp

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codingwithme.notesapp.database.NotesDatabase
import com.codingwithme.notesapp.database.TaskDatabase
import com.codingwithme.notesapp.databinding.FragmentCreateTaskBinding
import com.codingwithme.notesapp.databinding.FragmentHomeBinding
import com.codingwithme.notesapp.entities.Notes
import com.codingwithme.notesapp.entities.Task
import com.codingwithme.notesapp.util.NoteBottomSheetFragment
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*


class CreateTaskFragment :BaseFragment(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks{

    var selectedColor = "#171C26"
    var currentDate:String? = null
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE = 456
    private var selectedImagePath = ""
    private var webLink = ""
    private var taskId = -1

    private var _binding: FragmentCreateTaskBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskId = requireArguments().getInt("taskId",-1)

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            CreateTaskFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (taskId != -1){

            launch {
                context?.let {
                    var tasks = TaskDatabase.getDatabase(it).taskDao().getSpecificTask(taskId)
                    _binding?.colorView?.setBackgroundColor(Color.parseColor(tasks.color))
                    _binding?.etNoteTitle?.setText(tasks.title)
                    _binding?.etNoteSubTitle?.setText(tasks.subTitle)
                    _binding?.etNoteDesc?.setText(tasks.noteText)
                    if (tasks.imgPath != ""){
                        selectedImagePath = tasks.imgPath!!
                        _binding?.imgNote?.setImageBitmap(BitmapFactory.decodeFile(tasks.imgPath))
                        _binding?.layoutImage?.visibility = View.VISIBLE
                        _binding?.imgNote?.visibility = View.VISIBLE
                        _binding?.imgDelete?.visibility = View.VISIBLE
                    }else{
                        _binding?.layoutImage?.visibility = View.GONE
                        _binding?.imgNote?.visibility = View.GONE
                        _binding?.imgDelete?.visibility = View.GONE
                    }

                    if (tasks.webLink != ""){
                        webLink = tasks.webLink!!
                        _binding?.tvWebLink?.text = tasks.webLink
                        _binding?.layoutWebUrl?.visibility = View.VISIBLE
                        _binding?.etWebLink?.setText(tasks.webLink)
                        _binding?.imgUrlDelete?.visibility = View.VISIBLE
                    }else{
                        _binding?.imgUrlDelete?.visibility = View.GONE
                        _binding?.layoutWebUrl?.visibility = View.GONE
                    }
                }
            }
        }
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        currentDate = sdf.format(Date())
        _binding?.colorView?.setBackgroundColor(Color.parseColor(selectedColor))

        _binding?.tvDateTime?.text = currentDate

        _binding?.imgDone?.setOnClickListener {
            if (taskId != -1){
                updateTask()
            }else{
                saveTask()
            }
        }

        _binding?.imgBack?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        _binding?.imgMore?.setOnClickListener{


            var noteBottomSheetFragment = NoteBottomSheetFragment.newInstance(taskId)
            noteBottomSheetFragment.show(requireActivity().supportFragmentManager,"task Bottom Sheet Fragment")
        }

        _binding?.imgDelete?.setOnClickListener {
            selectedImagePath = ""
            _binding?.layoutImage?.visibility = View.GONE

        }

        _binding?.btnOk?.setOnClickListener {
            if (_binding?.etWebLink?.text.toString().trim().isNotEmpty()){
                checkWebUrl()
            }else{
                Toast.makeText(requireContext(),"Url is Required", Toast.LENGTH_SHORT).show()
            }
        }

        _binding?.btnCancel?.setOnClickListener {
            if (taskId != -1){
                _binding?.tvWebLink?.visibility = View.VISIBLE
                _binding?.layoutWebUrl?.visibility = View.GONE
            }else{
                _binding?.layoutWebUrl?.visibility = View.GONE
            }

        }

        _binding?.imgUrlDelete?.setOnClickListener {
            webLink = ""
            _binding?.tvWebLink?.visibility = View.GONE
            _binding?.imgUrlDelete?.visibility = View.GONE
            _binding?.layoutWebUrl?.visibility = View.GONE
        }

        _binding?.tvWebLink?.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(_binding?.etWebLink?.text.toString()))
            startActivity(intent)
        }

    }


    private fun updateTask(){
        launch {

            context?.let {
                var task = TaskDatabase.getDatabase(it).taskDao().getSpecificTask(taskId)

                task.title = _binding?.etNoteTitle?.text.toString()
                task.subTitle = _binding?.etNoteSubTitle?.text.toString()
                task.noteText = _binding?.etNoteDesc?.text.toString()
                task.dateTime = currentDate
                task.color = selectedColor
                task.imgPath = selectedImagePath
                task.webLink = webLink

                TaskDatabase.getDatabase(it).taskDao().updateTask(task)
                _binding?.etNoteTitle?.setText("")
                _binding?.etNoteSubTitle?.setText("")
                _binding?.etNoteDesc?.setText("")
                _binding?.layoutImage?.visibility = View.GONE
                _binding?.imgNote?.visibility = View.GONE
                _binding?.tvWebLink?.visibility = View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }
    private fun saveTask(){

        if (_binding?.etNoteTitle?.text.isNullOrEmpty()){
            Toast.makeText(context,"Note Title is Required", Toast.LENGTH_SHORT).show()
        }
        else if (_binding?.etNoteSubTitle?.text.isNullOrEmpty()){

            Toast.makeText(context,"Note Sub Title is Required", Toast.LENGTH_SHORT).show()
        }

        else if (_binding?.etNoteDesc?.text.isNullOrEmpty()){

            Toast.makeText(context,"Note Description is Required", Toast.LENGTH_SHORT).show()
        }

        else{

            launch {
                var task = Task()
                task.title = _binding?.etNoteTitle?.text.toString()
                task.subTitle = _binding?.etNoteSubTitle?.text.toString()
                task.noteText = _binding?.etNoteDesc?.text.toString()
                task.dateTime = currentDate
                task.color = selectedColor
                task.imgPath = selectedImagePath
                task.webLink = webLink
                context?.let {
                    TaskDatabase.getDatabase(it).taskDao().insertTasks(task)
                    _binding?.etNoteTitle?.setText("")
                    _binding?.etNoteSubTitle?.setText("")
                    _binding?.etNoteDesc?.setText("")
                    _binding?.layoutImage?.visibility = View.GONE
                    _binding?.imgNote?.visibility = View.GONE
                    _binding?.tvWebLink?.visibility = View.GONE
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

    }

    private fun deletetask(){

        launch {
            context?.let {
                TaskDatabase.getDatabase(it).taskDao().deleteSpecificTask(taskId)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }

    private fun checkWebUrl(){
        if (Patterns.WEB_URL.matcher(_binding?.etWebLink?.text.toString()).matches()){
            _binding?.layoutWebUrl?.visibility = View.GONE
            _binding?.etWebLink?.isEnabled = false
            webLink = _binding?.etWebLink?.text.toString()
            _binding?.tvWebLink?.visibility = View.VISIBLE
            _binding?.tvWebLink?.text = _binding?.etWebLink?.text.toString()
        }else{
            Toast.makeText(requireContext(),"Url is not valid", Toast.LENGTH_SHORT).show()
        }
    }


    private val BroadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {

            var actionColor = p1!!.getStringExtra("action")

            when(actionColor!!){

                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    _binding?.colorView?.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "Yellow" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    _binding?.colorView?.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    _binding?.colorView?.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    _binding?.colorView?.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    _binding?.colorView?.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Black" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    _binding?.colorView?.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "Image" ->{
                    readStorageTask()
                    _binding?.layoutWebUrl?.visibility = View.GONE
                }

                "WebUrl" ->{
                    _binding?.layoutWebUrl?.visibility = View.VISIBLE
                }
                "DeleteNote" -> {
                    //delete note
                    deletetask()
                }


                else -> {
                    _binding?.layoutImage?.visibility = View.GONE
                    _binding?.imgNote?.visibility = View.GONE
                    _binding?.layoutWebUrl?.visibility = View.GONE
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    _binding?.colorView?.setBackgroundColor(Color.parseColor(selectedColor))

                }
            }
        }

    }

    override fun onDestroy() {

        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()


    }

    private fun hasReadStoragePerm():Boolean{
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    private fun readStorageTask(){
        if (hasReadStoragePerm()){


            pickImageFromGallery()
        }else{
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun pickImageFromGallery(){
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null){
            startActivityForResult(intent,REQUEST_CODE_IMAGE)
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath:String? = null
        var cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK){
            if (data != null){
                var selectedImageUrl = data.data
                if (selectedImageUrl != null){
                    try {
                        var inputStream = requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        _binding?.imgNote?.setImageBitmap(bitmap)
                        _binding?.imgNote?.visibility = View.VISIBLE
                        _binding?.layoutImage?.visibility = View.VISIBLE

                        selectedImagePath = getPathFromUri(selectedImageUrl)!!
                    }catch (e:Exception){
                        Toast.makeText(requireContext(),e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,requireActivity())
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(),perms)){
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }

    override fun onRationaleAccepted(requestCode: Int) {

    }

}

