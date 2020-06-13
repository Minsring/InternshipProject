package com.test.internship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.test.internship.Register_Activity.person1_n;
import static com.test.internship.Register_Activity.person1_p;
import static com.test.internship.Register_Activity.person2_n;
import static com.test.internship.Register_Activity.person2_p;
import static com.test.internship.Register_Activity.person3_n;
import static com.test.internship.Register_Activity.person3_p;
import static com.test.internship.Register_Activity.person4_n;
import static com.test.internship.Register_Activity.person4_p;
import static com.test.internship.Register_Activity.person5_n;
import static com.test.internship.Register_Activity.person5_p;
import static com.test.internship.Register_Activity.persondata;

///커스텀 리스너 정의 -> 추가
public class CustomAdapter extends BaseAdapter {

//    Context context;
//    LayoutInflater layoutInflater;
//    ArrayList<String>data;
//    ListView listView;


    private Context mcontext = null;
    private ArrayList<ProtectorData> mdata = null;
    private int layout = 0;
    private LayoutInflater inflater = null;
    int num, t;
    public CustomAdapter(Context context, int layout, ArrayList<ProtectorData> mdata) {
        this.mcontext = context;
        this.layout = layout;
        this.mdata = mdata;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position).getPersonname();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }
        ImageView img = convertView.findViewById(R.id.img);
        TextView personname = convertView.findViewById(R.id.personname);
        TextView personnum = convertView.findViewById(R.id.personnum);
//        Button btndelete = convertView.findViewById(R.id.btndelete);
        LinearLayout item_layout = (LinearLayout) convertView.findViewById(R.id.item_layout);

        img.setImageResource(mdata.get(position).getImg());
        personname.setText(mdata.get(position).getPersonname());
        personnum.setText(mdata.get(position).getPersonnum());
        System.out.println("1");


        item_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                num=0;
                t=0;
                if(position==0){
                    if(person1_n==null && person1_p==null){
                        if(person2_n == null && person2_p==null){
                            if(person3_n==null && person3_p==null){
                                if(person4_n==null && person4_p==null){
                                    person5_n=null;
                                    person5_p=null;
                                }
                                else{
                                    person4_p=null;
                                    person4_n=null;
                                }
                            }
                            else{
                                person3_p=null;
                                person3_n=null;
                            }
                        }
                        else{
                            person2_p=null;
                            person2_n=null;
                        }
                    }
                    else {
                        person1_n = null;
                        person1_p = null;
                    }
                }
                else if(position==1){
                    while (t<5){
                        if(t==0){
                            if(person1_p!=null&&person1_n!=null){
                                num++;
                            }
                            t++;
                        }
                        else if (t==1){
                            if(person2_p!=null&&person2_n!=null){
                                if(num==1){
                                    person2_p=null;
                                    person2_n=null;
                                    break;
                                }
                                num++;
                            }
                            t++;
                        }
                        else if(t==2){
                            if(person3_n!=null&&person3_p!=null){
                                if(num==1){
                                    person3_n=null;
                                    person3_p=null;
                                    break;
                                }
                                else{
                                    num++;
                                }
                            }
                            t++;
                        }
                        else if(t==3){
                            if(person4_n!=null&&person4_p!=null){
                                if(num==1){
                                    person4_n=null;
                                    person4_p=null;
                                    break;
                                }
                                else{
                                    num++;
                                }
                            }
                            t++;
                        }
                        else{
                            if(person5_n!=null&&person5_p!=null){
                                if(num==1){
                                    person5_n=null;
                                    person5_p=null;
                                    break;
                                }
                                else{
                                    num++;
                                }
                            }
                            t++;
                        }
                    }
                }
                else if(position==2){
                   while (t<5){
                       if(t==0){
                           if(person1_p!=null&&person1_n!=null){
                               num++;
                           }
                           t++;
                       }
                       else if (t==1){
                           if(person2_p!=null&&person2_n!=null){
                               num++;
                           }
                           t++;
                       }
                       else if(t==2){
                           if(person3_n!=null&&person3_p!=null){
                               if(num==2){
                                   person3_n=null;
                                   person3_p=null;
                                   break;
                               }
                               else{
                                   num++;
                               }
                           }
                           t++;
                       }
                       else if(t==3){
                           if(person4_n!=null&&person4_p!=null){
                               if(num==2){
                                   person4_n=null;
                                   person4_p=null;
                                   break;
                               }
                               else{
                                   num++;
                               }
                           }
                           t++;
                       }
                       else{
                           if(person5_n!=null&&person5_p!=null){
                               if(num==2){
                                   person5_n=null;
                                   person5_p=null;
                                   break;
                               }
                               else{
                                   num++;
                               }
                           }
                           t++;
                       }
                   }
                }
                else if(position==3){
                    while (t<5){
                        if(t==0){
                            if(person1_p!=null&&person1_n!=null){
                                num++;
                            }
                            t++;
                        }
                        else if (t==1){
                            if(person2_p!=null&&person2_n!=null){
                                num++;
                            }
                            t++;
                        }
                        else if(t==2){
                            if(person3_n!=null&&person3_p!=null){
                                if(num==3){
                                    person3_n=null;
                                    person3_p=null;
                                    break;
                                }
                                else{
                                    num++;
                                }
                            }
                            t++;
                        }
                        else if(t==3){
                            if(person4_n!=null&&person4_p!=null){
                                if(num==3){
                                    person4_n=null;
                                    person4_p=null;
                                    break;
                                }
                                else{
                                    num++;
                                }
                            }
                            t++;
                        }
                        else{
                            if(person5_n!=null&&person5_p!=null){
                                if(num==3){
                                    person5_n=null;
                                    person5_p=null;
                                    break;
                                }
                                else{
                                    num++;
                                }
                            }
                            t++;
                        }
                    }
                }
                else{
                    person5_n=null;
                    person5_p=null;
                }
                persondata.remove(position);
                Register_Activity.customAdapter.notifyDataSetChanged();
//                Toast.makeText(Register_Activity, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

//        View view = layoutInflater.inflate(R.layout.protectorinfo_style,null);
//        TextView t1 = view.findViewById(R.id.personinfo);
//        t1.setText(data.get(position));


//        View bodyView = view.findViewById(R.id.body);
//        Button btn = view.findViewById(R.id.btndelete);
//
//        bodyView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Toast.makeText(context,"바디클릭 테스트",Toast.LENGTH_SHORT).show();
//            }
//        });
        return convertView;
    }
}

