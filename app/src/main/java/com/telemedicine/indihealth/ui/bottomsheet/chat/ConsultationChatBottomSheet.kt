package com.telemedicine.indihealth.ui.bottomsheet.chat

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.LayoutChatBinding
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.model.Chat
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import kotlinx.android.synthetic.main.layout_chat.*
import timber.log.Timber
import java.io.File


class ConsultationChatBottomSheet(
    private val scheduleDoctorSchedule: ScheduleDoctorConsultation?,
    private val chatTitle: String
) : BottomSheetDialogFragment() {

    private val viewModel: ConsultationChatViewModel by activityViewModels()
    private lateinit var adapter: ConsultationChatAdapter

    var onNavigationClicked: (() -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutChatBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                title = chatTitle
                vm = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chat_toolbar.setBackgroundColor(ResourcesCompat.getColor(view.context.resources,R.color.colorPrimary,null))
        setFirebaseDatabase()
        setObservableValue()
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized)
            adapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        if (::adapter.isInitialized)
            adapter.stopListening()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::adapter.isInitialized)
            adapter.stopListening()
    }

    private fun setOnClickListener() {
        chat_iv_attachment.setOnClickListener {
            ImagePicker.with(this)
//                .compress(1024)
//                .maxResultSize(620, 620)
                .start()
        }
        chat_iv_close.setOnClickListener {
            setImageEmpty()
        }
        chat_fab_send.setOnClickListener{
            Timber.d("chatText = ${viewModel.chatText.value}")
            if(viewModel.fileToUpload != null){
                Timber.d("chatText post Image = ${viewModel.fileToUpload} and chattext =${viewModel.chatText.value}")
                viewModel.postChatImage()
            } else if (viewModel.chatText.value != null) {
                Timber.d("sendToFirebase Image = ${viewModel.fileToUpload} and chattext =${viewModel.chatText.value}")
                viewModel.sendToFirebaseDatabase()
            }
            setImageEmpty()
        }
        chat_toolbar.setOnClickListener {
            onNavigationClicked?.invoke()
        }
    }

    private fun setObservableValue() {
        viewModel.scheduleDoctorConsultation = scheduleDoctorSchedule
        viewModel.responseStatus.observe(this, {
            it.getContentIfNotHandled()?.apply {
                Toast.makeText(requireContext(), this.getValue("msg").toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setFirebaseDatabase() {
        viewModel.child = "${AppVar.PATH_CHATS}/${scheduleDoctorSchedule
            ?.id_dokter}_${scheduleDoctorSchedule?.id_pasien}"
        Timber.d("child = ${viewModel.child}")
        val messagesRef: DatabaseReference = viewModel.dbReference.child(viewModel.child).apply {
            addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Timber.d("dataSnapshot = ${dataSnapshot.childrenCount}")
                        chat_et_chat.isEnabled = true
                        chat_fab_send.isEnabled = true
                    } else {
                        chat_et_chat.isEnabled = true
                        chat_fab_send.isEnabled = true
                    }
                    Timber.d("dataSnapshot = ${dataSnapshot.children}")
                    Timber.d("dataSnapshotCount = ${dataSnapshot.childrenCount}")
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        val options = FirebaseRecyclerOptions.Builder<Chat>()
            .setQuery(messagesRef, viewModel.parser)
            .build()

        //setup view
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        chat_rv_chat.layoutManager = layoutManager

        //setup adapter
        Timber.d("getUserRole = ${viewModel.getUserRole()}")
        adapter = ConsultationChatAdapter(options, viewModel.getUserRole())
        adapter.add(scheduleDoctorSchedule)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val friendlyMessageCount: Int = adapter.itemCount
                val lastVisiblePosition: Int =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.

                if (lastVisiblePosition == -1 ||
                    positionStart >= friendlyMessageCount - 1
                ) {
                    chat_rv_chat.scrollToPosition(positionStart)
                }
            }
        })
        chat_rv_chat.adapter = adapter
        adapter.startListening()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            setBottomSheetBehavior(getBottomSheet(dialog))
        }
        return dialog
    }

    private fun getBottomSheet(dialog: Dialog): View {
        val bottomSheet: FrameLayout =
            dialog.findViewById(R.id.design_bottom_sheet)
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        return bottomSheet
    }

    private fun setBottomSheetBehavior(view: View) {
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(view)
        behavior.skipCollapsed = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Timber.d("newState = $newState peekheight = ${behavior.peekHeight}")
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data

                //You can get File object from intent
                val file: File = ImagePicker.getFile(data)!!
                val fileSize = file.sizeInKb
                Timber.d("Size ini tuh %s", fileSize)
                if(fileSize > 7168){
                    Toast.makeText(requireContext(), "Maksimal file untuk diupload 7MB", Toast.LENGTH_SHORT).show()
                }else {
                    setImage(fileUri)
                    viewModel.setPhoto(file)
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
                setImageEmpty()
                viewModel.fileToUpload = null
            }
            else -> {
                Toast.makeText(requireContext(), "Cari foto dibatalkan", Toast.LENGTH_SHORT).show()
                setImageEmpty()
                viewModel.fileToUpload = null
            }
        }
    }

    private fun setImage(uri: Uri?) {
        chat_cv_image.visibility = View.VISIBLE
        chat_iv_image.setImageURI(uri)
        viewModel.fileName = uri.toString().substringAfterLast("/")
    }

    private fun setImageEmpty() {
        chat_cv_image.visibility = View.GONE
    }

    val File.size get() = if (!exists()) 0.0 else length().toDouble()
    val File.sizeInKb get() = size / 1024
}