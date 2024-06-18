import SetImageUtil.setImageToImageView
import android.content.Context
import android.util.Log
import android.view.View
import com.example.furuyonideckmanager.Deck
import io.realm.OrderedRealmCollection
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.furuyonideckmanager.R
import com.example.furuyonideckmanager.ThreeMegami

class ThreeMegamiAdapter(private val data: OrderedRealmCollection<ThreeMegami>, private val context: Context) :
    RecyclerView.Adapter<ThreeMegamiAdapter.ViewHolder>() {

    interface Listener {
        fun onGroupDeleteButtonClick(view: View, item: ThreeMegami);
        fun onGroupNameButtonClick(view: View, item: ThreeMegami);
    }

    private var listener: Listener? = null;

    init {
        setHasStableIds(true);
    }
    
    class ViewHolder(cell: View): RecyclerView.ViewHolder(cell) {
        // パーツを保持しておく場所
        val deleteButton: Button = cell.findViewById(R.id.deleteButton);
        val megami0: ImageView = cell.findViewById(R.id.megamiImage0);
        val megami1: ImageView = cell.findViewById(R.id.megamiImage1);
        val megami2: ImageView = cell.findViewById(R.id.megamiImage2);
        val showGroupButton: Button = cell.findViewById(R.id.showGroupButton);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        val view = inflater.inflate(R.layout.threemegami_item, null);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group: ThreeMegami = data.get(position);
        // データ設定
        holder.deleteButton.setOnClickListener {
            listener?.onGroupDeleteButtonClick(it, group)
        }

        setImageToImageView(group.megami0 + ".jpg", holder.megami0, context.resources.assets);
        setImageToImageView(group.megami1 + ".jpg", holder.megami1, context.resources.assets);
        setImageToImageView(group.megami2 + ".jpg", holder.megami2, context.resources.assets);
        holder.showGroupButton.text = group.title;
        holder.showGroupButton.setOnClickListener {
            listener?.onGroupNameButtonClick(it, group);
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener;
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getItemViewType(position: Int): Int {
        return position;
    }
}