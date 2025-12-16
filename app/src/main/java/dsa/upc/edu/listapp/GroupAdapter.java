// IMPORTANT!!!
// Creació de GroupAdapter per no provocar erros pero seguiNt el mateix patró que MyAdapter
package dsa.upc.edu.listapp;



import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import dsa.upc.edu.listapp.github.Group;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    public interface OnJoinClickListener {
        void onJoin(Group group);
    }

    private List<Group> values;
    private OnJoinClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvGroupName;
        public TextView tvGroupId;
        public Button btnJoinGroup;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            tvGroupName = (TextView) v.findViewById(R.id.tvGroupName);
            tvGroupId = (TextView) v.findViewById(R.id.tvGroupId);
            btnJoinGroup = (Button) v.findViewById(R.id.btnJoinGroup);
        }
    }

    public GroupAdapter(OnJoinClickListener listener) {
        this.values = null;
        this.listener = listener;
    }

    public void setData(List<Group> dataset) {
        this.values = dataset;
        notifyDataSetChanged();

    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_group_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Group group = values.get(position);


        holder.tvGroupName.setText(group.name);
        holder.tvGroupId.setText("ID: " + group.id);

        holder.btnJoinGroup.setOnClickListener(v -> {
            if (listener != null) listener.onJoin(group);
        });
    }

    @Override
    public int getItemCount() {
        if (values == null) return 0;
        return values.size();
    }
}

