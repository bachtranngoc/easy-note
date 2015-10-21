package org.bkap.easynote.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.bkap.easynote.models.Note;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nguye on 8/21/2015.
 */
public class MyNoteAdapter extends
        ArrayAdapter<Note> {
    Activity context = null;
    ArrayList<Note> myArray = null;
    int layoutId;

    /**
     * Constructor này dùng để khởi tạo các giá trị
     * từ MainActivity truyền vào
     *
     * @param context   : là Activity từ Main
     * @param layoutId: Là layout custom do ta tạo (my_item_layout.xml)
     * @param arr       : Danh sách nhân viên truyền từ Main
     */
    public MyNoteAdapter(Activity context,
                         int layoutId,
                         ArrayList<Note> arr) {
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    /**
     * hàm dùng để custom layout, ta phải override lại hàm này
     * từ MainActivity truyền vào
     *
     * @param position     : là vị trí của phần tử trong danh sách nhân viên
     * @param convertView: convertView, dùng nó để xử lý Item
     * @param parent       : Danh sách nhân viên truyền từ Main
     * @return View: trả về chính convertView
     */
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        /**
         * bạn chú ý là ở đây Tôi không làm:
         * if(convertView==null)
         * {
         * LayoutInflater inflater=
         * context.getLayoutInflater();
         * convertView=inflater.inflate(layoutId, null);
         * }
         * Lý do là ta phải xử lý xóa phần tử Checked, nếu dùng If thì
         * nó lại checked cho các phần tử khác sau khi xóa vì convertView
         * lưu lại trạng thái trước đó
         */
        LayoutInflater inflater =
                context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, parent, false);
        //chỉ là test thôi, bạn có thể bỏ If đi
        if (myArray.size() > 0 && position >= 0) {
            //dòng lệnh lấy TextView ra để hiển thị Mã và tên lên
            final ImageView iv_delete = (ImageView) convertView.findViewById(R.id.bt_delete_note);

            final TextView tv_Title = (TextView)
                    convertView.findViewById(R.id.tv_title);
            final TextView tv_Description = (TextView)
                    convertView.findViewById(R.id.tv_description);
            final TextView tv_Date = (TextView)
                    convertView.findViewById(R.id.tv_date);
            //lấy ra nhân viên thứ position
            final Note note = myArray.get(position);
            //đưa thông tin lên TextView
            final String date = getDate(note.getAddedDate());

            tv_Title.setText(note.getTitle());
            tv_Description.setText(note.getContent());
            tv_Date.setText(date);
            //lấy ImageView ra để thiết lập hình ảnh cho đúng

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            getContext());
                    alert.setTitle("Xóa Note!");
                    alert.setMessage("Bạn có chắc muốn xóa note?");
                    alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do your work here
                            new REMOVESyntask().execute(String.valueOf(note.getAddedDate()));
                            myArray.remove(position);
                            Toast.makeText(getContext(), "Đã xóa \"" + note.getTitle() + "\" khỏi danh sách", Toast.LENGTH_SHORT).show();
                            MainActivity.adapter.notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });
                    alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }
            });

        }
        //Vì View là Object là dạng tham chiếu đối tượng, nên
        //mọi sự thay đổi của các object bên trong convertView
        //thì nó cũng biết sự thay đổi đó
        return convertView;//trả về View này, tức là trả luôn
        //về các thông số mới mà ta vừa thay đổi
    }

    private String getDate(long time) {
        Date date = new Date(time);
        return date.toString();
    }

    //remove 1
    private class REMOVESyntask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            MyDatabase db = new MyDatabase(getContext());

            // Reading all contacts
            Log.d("REMOVING: ", "REMOVING one...");

            db.deleteNote(params[0]);
            return null;
        }
    }
}