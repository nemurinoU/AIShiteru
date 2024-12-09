package com.prototype.aishiteru.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.prototype.aishiteru.databinding.FragmentMapBinding

// For location and fused location provider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Location
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import android.location.Geocoder
import android.content.res.ColorStateList
import com.prototype.aishiteru.R
import java.util.Locale
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MapFragment : Fragment(), OnMapReadyCallback {
    // these 4 are for location services
    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // List of catch areas
    private val catchAreas = mutableListOf<LatLng>()

    // Original color of the button
    private lateinit var originalButtonColor: ColorStateList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Save original button color and set it to grey when inactive
        originalButtonColor = binding.efabCheckin.backgroundTintList ?: ColorStateList.valueOf(0xFF6200EE.toInt())
        val greyColor = android.graphics.Color.argb(
            android.graphics.Color.alpha(originalButtonColor.defaultColor),
            (android.graphics.Color.red(originalButtonColor.defaultColor) * 0.3).toInt(),
            (android.graphics.Color.green(originalButtonColor.defaultColor) * 0.3).toInt(),
            (android.graphics.Color.blue(originalButtonColor.defaultColor) * 0.3).toInt()
        )

        binding.efabCheckin.isEnabled = false
        binding.efabCheckin.visibility = View.VISIBLE
        binding.efabCheckin.backgroundTintList = ColorStateList.valueOf(greyColor)

        binding.efabCheckin.setOnClickListener {
            val randomNumber = Random.nextInt(1, 5) // Random number between 1-4
            val currentLocationName = getCurrentPlaceName(currentLocation?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(0.0, 0.0))
            Toast.makeText(requireContext(), "Checked-in at $currentLocationName!", Toast.LENGTH_SHORT).show()

            // TODO: Add code that transfers user to a gacha page that unlocks the character based on index randomNumber
            // TODO: And then add the character to the user's collection as well as redirect them to a chatroom with the context of location
        }

        getCurrentLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // function for checking location
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
            return
        }

        val getLocation = fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {
                currentLocation = location

                val exploreFragment = childFragmentManager
                    .findFragmentById(R.id.exploreMap) as SupportMapFragment
                exploreFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            )
                getCurrentLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Enable My Location layer
        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return // Permissions are not granted
        }

        mMap.isMyLocationEnabled = true // This shows the blue dot for current location
        currentLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f)) // Zoom level set to 20f for a closer view

            // Add random catch areas
            addCatchAreas(latLng)

            // Check if user is within a catch area when location changes
            mMap.setOnMyLocationChangeListener { location ->
                checkIfInCatchArea(LatLng(location.latitude, location.longitude))
            }
        } ?: run {
            Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to add random catch areas on the map
    private fun addCatchAreas(userLocation: LatLng) {
        for (i in 1..3) { // Adding 3 random catch areas
            var randomLatLng: LatLng
            do {
                val randomLat = userLocation.latitude + Random.nextDouble(-0.001, 0.001)
                val randomLng = userLocation.longitude + Random.nextDouble(-0.001, 0.001)
                randomLatLng = LatLng(randomLat, randomLng)
            } while (calculateDistance(userLocation, randomLatLng) < 0.03 || catchAreas.any { calculateDistance(it, randomLatLng) < 0.06 }) // Ensure it's not too close to the user

            catchAreas.add(randomLatLng)
            val placeName = getCurrentPlaceName(randomLatLng)
            mMap.addCircle(
                CircleOptions()
                    .center(randomLatLng)
                    .radius(30.0)
                    .strokeColor(0xFFFF0000.toInt()) // Red color for the stroke
                    .strokeWidth(2f)
                    .fillColor(0x44FF0000) // Light red fill color
                    .clickable(true)
            )
            mMap.addMarker(MarkerOptions().position(randomLatLng).title(placeName)) // Add marker to show place name
        }
    }

    // Function to check if user is inside any catch area
    private fun checkIfInCatchArea(userLatLng: LatLng) {
        for (catchArea in catchAreas) {
            val distance = calculateDistance(userLatLng, catchArea)
            if (distance <= 0.03) { // 30 meters radius
                binding.efabCheckin.isEnabled = true
                binding.efabCheckin.visibility = View.VISIBLE
                binding.efabCheckin.backgroundTintList = originalButtonColor
                return
            }
        }
        binding.efabCheckin.isEnabled = false
        binding.efabCheckin.visibility = View.VISIBLE
        binding.efabCheckin.backgroundTintList = ColorStateList.valueOf(android.graphics.Color.argb(
            android.graphics.Color.alpha(originalButtonColor.defaultColor),
            (android.graphics.Color.red(originalButtonColor.defaultColor) * 0.3).toInt(),
            (android.graphics.Color.green(originalButtonColor.defaultColor) * 0.3).toInt(),
            (android.graphics.Color.blue(originalButtonColor.defaultColor) * 0.3).toInt()
        ))
    }

    // Utility function to calculate distance between two LatLng points
    private fun getCurrentPlaceName(location: LatLng): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                addresses[0].getAddressLine(0) // Or use `addresses[0].featureName` for a more specific name
            } else {
                "Unknown Location"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown Location"
        }
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Double {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            start.latitude, start.longitude,
            end.latitude, end.longitude,
            results
        )
        return results[0].toDouble() / 1000 // return distance in kilometers
    }
}
