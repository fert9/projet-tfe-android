import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sannerqr.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView heure_arrivée,heure_depart;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        heure_arrivée=(TextView)itemView.findViewById(R.id.list);
        heure_depart=(TextView)itemView.findViewById(R.id.heure);
    }
}
