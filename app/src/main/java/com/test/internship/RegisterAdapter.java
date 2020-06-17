package com.test.internship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.test.internship.Register.person1_n;
import static com.test.internship.Register.person1_p;
import static com.test.internship.Register.person2_n;
import static com.test.internship.Register.person2_p;
import static com.test.internship.Register.person3_n;
import static com.test.internship.Register.person3_p;
import static com.test.internship.Register.person4_n;
import static com.test.internship.Register.person4_p;
import static com.test.internship.Register.person5_n;
import static com.test.internship.Register.person5_p;
import static com.test.internship.Register.persondata;

///커스텀 리스너 정의 -> 추가
public class RegisterAdapter extends BaseAdapter {

    private Context mcontext = null;
    private ArrayList<ProtectorData> mdata = null;
    private int layout = 0;
    private LayoutInflater inflater = null;
    int num, t;

    public RegisterAdapter(Context context, int layout, ArrayList<ProtectorData> mdata) {
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
                Register.registerAdapter.notifyDataSetChanged();
                Toast.makeText(mcontext, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return convertView;
    }
}

