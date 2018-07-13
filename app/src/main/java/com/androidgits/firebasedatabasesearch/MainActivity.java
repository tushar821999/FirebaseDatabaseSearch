package com.androidgits.firebasedatabasesearch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchField;
    private CircleImageView mSearchBtn;
    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (CircleImageView) findViewById(R.id.profile_image);
        mResultList = (RecyclerView) findViewById(R.id.result_list);

        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString().trim();
                firebaseUserSearch(searchText);
            }
        });

    }

    private void firebaseUserSearch(String searchText) {

        Toast.makeText(this, "Start Searching ...", Toast.LENGTH_SHORT).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");

        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.list_layout,
                UsersViewHolder.class,
                mUserDatabase

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {


                viewHolder.setDetails(getApplicationContext(),model.getName(),model.getStatus(),model.getImage());
            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

    }

    // view holder class

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context cntx,String userName, String userStatus, String userImage){
            CircleImageView user_image = (CircleImageView) mView.findViewById(R.id.profile_image);
            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);

            user_name.setText(userName);
            user_status.setText(userStatus);

            Glide.with(cntx).load(userImage).into(user_image);

        }

    }
}
