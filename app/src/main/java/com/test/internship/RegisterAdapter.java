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
import static com.test.internship.Register.personData;

public class RegisterAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ProtectorData> mData;
    private LayoutInflater inflater;
    private int layout;

    int numPerson, time;

    public RegisterAdapter(Context context, int layout, ArrayList<ProtectorData> mData) {
        this.mContext = context;
        this.layout = layout;
        this.mData = mData;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position).getPersonName();
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
        TextView personName = convertView.findViewById(R.id.personname);
        TextView personNum = convertView.findViewById(R.id.personnum);
        LinearLayout item_layout = convertView.findViewById(R.id.item_layout);

        img.setImageResource(mData.get(position).getImg());
        personName.setText(mData.get(position).getPersonName());
        personNum.setText(mData.get(position).getPersonNum());

        item_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                numPerson = 0;
                time = 0;
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
                    while (time <5){
                        if(time ==0){
                            if(person1_p!=null&&person1_n!=null){
                                numPerson++;
                            }
                            time++;
                        }
                        else if (time ==1){
                            if(person2_p!=null&&person2_n!=null){
                                if(numPerson ==1){
                                    person2_p=null;
                                    person2_n=null;
                                    break;
                                }
                                numPerson++;
                            }
                            time++;
                        }
                        else if(time ==2){
                            if(person3_n!=null&&person3_p!=null){
                                if(numPerson ==1){
                                    person3_n=null;
                                    person3_p=null;
                                    break;
                                }
                                else{
                                    numPerson++;
                                }
                            }
                            time++;
                        }
                        else if(time ==3){
                            if(person4_n!=null&&person4_p!=null){
                                if(numPerson ==1){
                                    person4_n=null;
                                    person4_p=null;
                                    break;
                                }
                                else{
                                    numPerson++;
                                }
                            }
                            time++;
                        }
                        else{
                            if(person5_n!=null&&person5_p!=null){
                                if(numPerson ==1){
                                    person5_n=null;
                                    person5_p=null;
                                    break;
                                }
                                else{
                                    numPerson++;
                                }
                            }
                            time++;
                        }
                    }
                }
                else if(position==2){
                   while (time <5){
                       if(time ==0){
                           if(person1_p!=null&&person1_n!=null){
                               numPerson++;
                           }
                           time++;
                       }
                       else if (time ==1){
                           if(person2_p!=null&&person2_n!=null){
                               numPerson++;
                           }
                           time++;
                       }
                       else if(time ==2){
                           if(person3_n!=null&&person3_p!=null){
                               if(numPerson ==2){
                                   person3_n=null;
                                   person3_p=null;
                                   break;
                               }
                               else{
                                   numPerson++;
                               }
                           }
                           time++;
                       }
                       else if(time ==3){
                           if(person4_n!=null&&person4_p!=null){
                               if(numPerson ==2){
                                   person4_n=null;
                                   person4_p=null;
                                   break;
                               }
                               else{
                                   numPerson++;
                               }
                           }
                           time++;
                       }
                       else{
                           if(person5_n!=null&&person5_p!=null){
                               if(numPerson ==2){
                                   person5_n=null;
                                   person5_p=null;
                                   break;
                               }
                               else{
                                   numPerson++;
                               }
                           }
                           time++;
                       }
                   }
                }
                else if(position==3){
                    while (time <5){
                        if(time ==0){
                            if(person1_p!=null&&person1_n!=null){
                                numPerson++;
                            }
                            time++;
                        }
                        else if (time ==1){
                            if(person2_p!=null&&person2_n!=null){
                                numPerson++;
                            }
                            time++;
                        }
                        else if(time ==2){
                            if(person3_n!=null&&person3_p!=null){
                                if(numPerson ==3){
                                    person3_n=null;
                                    person3_p=null;
                                    break;
                                }
                                else{
                                    numPerson++;
                                }
                            }
                            time++;
                        }
                        else if(time ==3){
                            if(person4_n!=null&&person4_p!=null){
                                if(numPerson ==3){
                                    person4_n=null;
                                    person4_p=null;
                                    break;
                                }
                                else{
                                    numPerson++;
                                }
                            }
                            time++;
                        }
                        else{
                            if(person5_n!=null&&person5_p!=null){
                                if(numPerson ==3){
                                    person5_n=null;
                                    person5_p=null;
                                    break;
                                }
                                else{
                                    numPerson++;
                                }
                            }
                            time++;
                        }
                    }
                }
                else{
                    person5_n=null;
                    person5_p=null;
                }
                personData.remove(position);
                Register.registerAdapter.notifyDataSetChanged();
                Toast.makeText(mContext, "선택한 보호자의 정보를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return convertView;
    }
}

