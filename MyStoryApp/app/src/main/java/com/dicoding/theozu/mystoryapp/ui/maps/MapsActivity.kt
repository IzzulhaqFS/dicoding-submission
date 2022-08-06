package com.dicoding.theozu.mystoryapp.ui.maps

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.theozu.mystoryapp.R
import com.dicoding.theozu.mystoryapp.ViewModelFactory
import com.dicoding.theozu.mystoryapp.api.response.ListStoryItem
import com.dicoding.theozu.mystoryapp.databinding.ActivityMapsBinding
import com.dicoding.theozu.mystoryapp.model.UserPreference
import com.dicoding.theozu.mystoryapp.ui.detail.DetailStoryActivity

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel
    private lateinit var iconGenerator: IconGenerator

    private val map = HashMap<Marker, ListStoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[MapsViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            viewModel.setListStory(user.token)
        }

        iconGenerator = IconGenerator(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        viewModel.listStory.observe(this@MapsActivity) { listStory ->
            for (story: ListStoryItem in listStory) {
                val position = LatLng(story.lat, story.lon)
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(story.name)
                        .snippet(story.description)
                        .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(story.name)))
                )
                marker?.let { map.put(it, story) }
            }
        }
        mMap.setOnMarkerClickListener {
            val story = map[it]
            val intent = Intent(this@MapsActivity, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.EXTRA_STORY, story)
            startActivity(intent)
            true
        }
    }
}