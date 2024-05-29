import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.databinding.NavLibraryBinding
import com.flowbyte.data.LibraryMenuItem

class RecylerNavLibraryAdapter(
    private val getSpecificFragment: () -> Fragment,
    private val navLibrary: List<LibraryMenuItem>
) : RecyclerView.Adapter<RecylerNavLibraryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = NavLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(navLibrary[position])
    }

    override fun getItemCount(): Int {
        return navLibrary.size
    }

    inner class MyViewHolder(private val binding: NavLibraryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(libraryMenuItem: LibraryMenuItem) {
            binding.buttonNavLibrary.text = libraryMenuItem.name
            binding.buttonNavLibrary.setOnClickListener {
                // Handle button click here
                // Example: Toast.makeText(itemView.context, "Clicked ${libraryMenuItem.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
