package uqac.ca.isstracker.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.squareup.picasso.Picasso

import java.util.ArrayList

import uqac.ca.isstracker.R
import uqac.ca.isstracker.model.Astronaut

// Provide a suitable constructor (depends on the kind of dataset)
class AstronautAdapter(private val appContext: Context, private val dataset: ArrayList<Astronaut>) : RecyclerView.Adapter<AstronautAdapter.CardViewHolder>()
{
    // Provide a reference to the views for each data item
    class CardViewHolder(v: View) : RecyclerView.ViewHolder(v)
    {
        var name: TextView
        var role: TextView
        var bio: TextView
        var launchDate: TextView
        var profileImage: ImageView
        var countryFlag: ImageView
        var astronautTwitter: ImageView
        var astronautWiki: ImageView

        init
        {
            this.name = v.findViewById(R.id.name)
            this.role = v.findViewById(R.id.role)
            this.bio = v.findViewById(R.id.bio)
            this.launchDate = v.findViewById(R.id.launchDate)

            this.profileImage = v.findViewById(R.id.img)
            this.countryFlag = v.findViewById(R.id.countryFlag)
            this.astronautTwitter = v.findViewById(R.id.twitter)
            this.astronautWiki = v.findViewById(R.id.wiki)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder
    {
        // create a new view
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.astro_card, parent, false)

        return CardViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CardViewHolder, position: Int)
    {
        // - get element from dataset at this position
        val astro = dataset[position]

        // - replace the contents of the view with that element
        //Texts
        holder.name.text = astro.name
        holder.role.text = "Role: " + astro.role
        holder.launchDate.text = "Launch date: " + astro.launchDate
        holder.bio.text = astro.bio

        //Images
        Picasso.get().load(astro.image).into(holder.profileImage)
        Picasso.get().load(astro.countryLink).into(holder.countryFlag)

        //Social networks
        if (astro.twitter.length == 0)
        {
            holder.astronautTwitter.visibility = View.INVISIBLE
        }
        else
        {
            holder.astronautTwitter.setOnClickListener {
                Log.d("ACTION", astro.twitter)
                appContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(astro.twitter)))
            }
        }

        if (astro.wiki.length == 0)
        {
            holder.astronautWiki.visibility = View.INVISIBLE
        }
        else
        {
            holder.astronautWiki.setOnClickListener {
                Log.d("ACTION", astro.wiki)
                appContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(astro.wiki)))
            }
        }
    }

    // Return the size of dataset (invoked by the layout manager)
    override fun getItemCount(): Int
    {
        return dataset.size
    }
}
