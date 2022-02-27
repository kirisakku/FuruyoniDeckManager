import SetImageUtil.setImageToImageView
import android.content.Context
import android.view.View
import com.example.furuyonideckmanager.Deck
import io.realm.OrderedRealmCollection
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.furuyonideckmanager.R

class DeckAdapter(private val data: OrderedRealmCollection<Deck>, private val context: Context) :
    RecyclerView.Adapter<DeckAdapter.ViewHolder>() {

    interface Listener {
        fun onDeleteButtonClick(view: View, item: Deck);
        fun onDeckNameButtonClick(view: View, item: Deck);
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
        val showDeckButton: Button = cell.findViewById(R.id.showDeckButton);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        val view = inflater.inflate(R.layout.deck_item, null);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deck: Deck = data.get(position);//data[position];
        // データ設定
        holder.deleteButton.setOnClickListener {
            listener?.onDeleteButtonClick(it, deck)
        }

        setImageToImageView(deck.megami0 + ".jpg", holder.megami0, context.resources.assets);
        setImageToImageView(deck.megami1 + ".jpg", holder.megami1, context.resources.assets);
        holder.showDeckButton.text = deck.title;
        holder.showDeckButton.setOnClickListener {
            listener?.onDeckNameButtonClick(it, deck);
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener;
    }
    override fun getItemCount(): Int {
        return data.size
    }
}