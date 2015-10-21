package org.bkap.easynote.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.bkap.easynote.models.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static ArrayList<Note> listNote;
    ListView lv, lv_dialog;
    static MyNoteAdapter adapter=null;
    Dialog dialog;
    int mPosition;
    Boolean isLong = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lv = (ListView) findViewById(R.id.lv_ds_note);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tạo Intent để mở ResultActivity
                Intent myIntent = new Intent(MainActivity.this, AddNoteAcitivity.class);
                //Mở Activity ResultActivity
                startActivity(myIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Load Note
        new mNoteSyncTask().execute();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ds_note) {
            // Handle the camera action
        } else if (id == R.id.nav_ds_todo) {

        } else if (id == R.id.nav_ql_the) {

        } else if (id == R.id.nav_tuychinh) {
            Intent intent = new Intent(this, CustomActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // My Asynctask_tracuu tab3
    private class mNoteSyncTask extends AsyncTask<String, Void, ArrayList<Note>> {
        long time_1, time_2;

        @Override
        protected ArrayList<Note> doInBackground(String... params) {
            MyDatabase db = new MyDatabase(getApplication());
            Log.d("Insert: ", "Inserting ..");
            // Reading all contacts
            Log.d("Reading: ", "Reading all Note...");

            listNote = db.getNote();

            for (Note note : listNote) {
                String log = "Title: " + note.getTitle() + ", Description: " + note.getContent() + ", Date: " + note.getAddedDate();
                // Writing Contacts to log
                Log.d("READ Note: ", log);

            }
//            Collections.sort(grammar, new Comparator<Grammar>() {
//                @Override
//                public int compare(Grammar lhs, Grammar rhs) {
//                    return lhs.getmStruct().compareTo(rhs.getmStruct());
//                }
//            });

            return listNote;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            time_1 = System.currentTimeMillis();
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            getListNote();
            time_2 = System.currentTimeMillis() - time_1;
            Log.d("query_3", String.valueOf(time_2));

        }
    }


    private void getListNote() {
        if (listNote == null) {
            return;
        }
        adapter = new MyNoteAdapter(this, R.layout.custom_list_note, listNote);
        lv.setAdapter(adapter);
//                                    tv_mMean.setText("");
//                                    lv.setVisibility(View.VISIBLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                                            lv.setVisibility(View.GONE);
//                                            String vMean = vija.get(position).getmMean().toString();
//                                            tv_mMean.setText(vMean);
//                                Toast.makeText(getContext(), vMean, Toast.LENGTH_SHORT).show();
                if (isLong) return;
                Intent myIntent = new Intent(getApplicationContext(), CustomNoteAcitivity.class);
                //Khai báo Bundle
                Bundle bundle = new Bundle();

                String title = listNote.get(position).getTitle();
                String des = listNote.get(position).getContent();
                int mPosition = position;
//
                //đưa dữ liệu riêng lẻ vào Bundle
                bundle.putString("title", title);
                bundle.putString("des", des);
                bundle.putInt("mPosition", mPosition);

//
                //Đưa Bundle vào Intent
                myIntent.putExtra("customNote", bundle);
//                Mở Activity ResultActivity
                startActivity(myIntent);

            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long mDate = listNote.get(position).getAddedDate();
                isLong = true;
                mPosition = position;
                showdialogDelete();
                lv_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                new REMOVESyntask().execute(String.valueOf(mDate));
                                listNote.remove(mPosition);
                                Toast.makeText(getApplicationContext(), "Đã xóa \"" + listNote.get(mPosition).getTitle() + "\" khỏi danh sách", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                                break;
                            case 1:
                                listNote.clear();
                                new REMOVEALLSyntask().execute();
                                Toast.makeText(getApplicationContext(), "Đã xóa các ghi chú khỏi danh sách", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                return false;
            }
        });
    }

    private void showdialogDelete() {
        // custom dialog
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        lv_dialog = (ListView) dialog.findViewById(R.id.lv_dialog);
        ArrayAdapter<String> adapter_dialog = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_list_item_1, getDetele());
        lv_dialog.setAdapter(adapter_dialog);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isLong = false;
            }
        });

    }

    private ArrayList<String> getDetele() {
        ArrayList<String> deleteList = new ArrayList<String>();
        deleteList.add("Xóa");
        deleteList.add("Xóa tất cả");

        return deleteList;
    }

    //remove 1
    private class REMOVESyntask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            MyDatabase db = new MyDatabase(getApplicationContext());

            // Reading all contacts
            Log.d("REMOVING: ", "REMOVING one...");

            db.deleteNote(params[0]);
            return null;
        }
    }

    //remove 1
    private class REMOVEALLSyntask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            MyDatabase db = new MyDatabase(getApplicationContext());

            // Reading all contacts
            Log.d("REMOVING: ", "REMOVING All...");

            db.deleteAllNote();
            return null;
        }
    }
}
