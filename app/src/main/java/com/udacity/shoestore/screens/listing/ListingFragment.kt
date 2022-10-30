package com.udacity.shoestore.screens.listing

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentListingBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.screens.SharedViewModel
import java.util.*

class ListingFragment : Fragment(), MenuProvider {

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentListingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shoeList: MutableList<Shoe> = viewModel.getShoeList()!!
        if (shoeList.isNotEmpty()) {
            submitList(shoeList)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listingFragment_to_detailFragment)
        }

        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "You Can`t go back", Toast.LENGTH_SHORT).show()
        }
        callback.isEnabled = true

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun submitList(shoeList: MutableList<Shoe>) {
        for (shoe in shoeList) {
            addItem(shoe)
        }
    }

    private fun addItem(shoe: Shoe) {
        // Get the widgets reference from XML layout
        val rootLayout = binding.llListShoe

        // Create a new childView instance programmatically
        val childView: View = layoutInflater.inflate(R.layout.list_item, null, false)
        // Creating a ConstraintLayout.LayoutParams object for child view
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, // This will define child view width
            LinearLayout.LayoutParams.WRAP_CONTENT // This will define child view height
        )
        // Add margin to the child view
        params.setMargins(10, 16, 10, 15)

        childView.apply {
            // Now, specify the child view width and height (dimension)
            layoutParams = params

            findViewById<TextView>(R.id.tv_name).text = shoe.name
            findViewById<TextView>(R.id.tv_size).text = shoe.size
            findViewById<TextView>(R.id.tv_company).text = shoe.company
            findViewById<TextView>(R.id.tv_description).text = shoe.description
            // Change the child view background color
            setBackgroundColor(Color.parseColor("#FEFEFA"))
            // Put some padding on child view
            setPadding(100, 100, 100, 100)
            // Set a click listener for newly generated child view
            childView.setOnClickListener {
                Toast.makeText(
                    requireContext(), "ChildView clicked.", Toast.LENGTH_SHORT
                ).show()
            }
            // Set an id for the child view
            childView.id = Random().nextInt()
            // Finally, add the child view to the view group
            rootLayout.addView(childView)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.logout_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // Handle the menu selection
        when (menuItem.itemId) {
            R.id.logout -> {
                findNavController().navigate(R.id.action_listingFragment_to_loginFragment)
            }
        }
        return false
    }
}