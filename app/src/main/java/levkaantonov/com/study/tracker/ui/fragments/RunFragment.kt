package levkaantonov.com.study.tracker.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import levkaantonov.com.study.tracker.R
import levkaantonov.com.study.tracker.databinding.FragmentRunBinding
import levkaantonov.com.study.tracker.other.Constants.REQUEST_CODE_LOCATION_PERMISSIONS
import levkaantonov.com.study.tracker.other.TrackingUtility
import levkaantonov.com.study.tracker.ui.adapters.RunAdapter
import levkaantonov.com.study.tracker.ui.viewmodels.MainViewModel
import levkaantonov.com.study.tracker.ui.viewmodels.SortType
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private var runAdapter: RunAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()
        setupRecyclerView()

        viewModel.sortType.observe(viewLifecycleOwner) { type ->
            binding.apply {
                when (type) {
                    SortType.DATE -> spFilter.setSelection(0)
                    SortType.RUNNING_TIME -> spFilter.setSelection(1)
                    SortType.DISTANCE -> spFilter.setSelection(2)
                    SortType.AVG_SPEED -> spFilter.setSelection(3)
                    SortType.CALORIES_BURNED -> spFilter.setSelection(4)
                }
            }
        }

        viewModel.runs.observe(viewLifecycleOwner) {
            runAdapter?.submitList(it)
        }
        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> viewModel.changeSortType(SortType.DATE)
                    1 -> viewModel.changeSortType(SortType.RUNNING_TIME)
                    2 -> viewModel.changeSortType(SortType.DISTANCE)
                    3 -> viewModel.changeSortType(SortType.AVG_SPEED)
                    4 -> viewModel.changeSortType(SortType.CALORIES_BURNED)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.fab.setOnClickListener {
            val action = RunFragmentDirections.actionRunFragmentToTrackingFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            rvRuns.apply {
                runAdapter = RunAdapter().apply { adapter = this }
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationsPermissions(requireContext())) {
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.warning_request_location_permissions),
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.warning_request_location_permissions),
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}