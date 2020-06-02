package com.example.highschoolalltime.adapter;

import java.util.ArrayList;

import com.example.highschoolalltime.Cafeteria;
import com.example.highschoolalltime.R;
import com.example.highschoolalltime.domain.DayInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/*BaseAdapter를 상속받은 adapter이다.
gridview는 adapter를 사용하여 gridview에 들어갈 데이터를 관리한다.*/
public class cafeteria_Adapter extends BaseAdapter
{
    private ArrayList<DayInfo> mDayList;
    private Context mContext;
    private int mResource;
    private LayoutInflater mLiInflater;
    //Adapter생성
    public cafeteria_Adapter(Context context, int textResource, ArrayList<DayInfo> dayList)
    {
        this.mContext = context;
        this.mDayList = dayList;
        this.mResource = textResource;
        this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    //날짜크기반환
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mDayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mDayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        DayInfo day = mDayList.get(position);

        DayViewHolde dayViewHolder;
        //처음실행시(null일때)
        if(convertView == null)
        {
            convertView = mLiInflater.inflate(mResource, null);
            //토요일(일주일의 마지막일)이면 줄바꿈
            if(position % 7 == 6)
            {
                convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));
            }
            else
            {
                convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP()));
            }

            dayViewHolder = new DayViewHolde();

            dayViewHolder.llBackground = (LinearLayout)convertView.findViewById(R.id.day_cell_ll_background);
            dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.day_cell_tv_day);

            convertView.setTag(dayViewHolder);
        }
        else
        {
            dayViewHolder = (DayViewHolde) convertView.getTag();
        }
        //day에 값이 있을때
        if(day != null)
        {
            dayViewHolder.tvDay.setText(day.getDay());
            //이번달이라면
            if(day.isInMonth())
            {
                //주의 첫 날(일요일)일때 빨간색
                if(position % 7 == 0)
                {
                    dayViewHolder.tvDay.setTextColor(Color.RED);
                }//주의 마지막 날(토요일)일때 파란색
                else if(position % 7 == 6)
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLUE);
                }//나머지 검정색
                else
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLACK);
                }
            }
            else//이번달이 아닐경우 회색
            {
                dayViewHolder.tvDay.setTextColor(Color.GRAY);
            }
        }
        return convertView;
    }

    public class DayViewHolde
    {
        public LinearLayout llBackground;
        public TextView tvDay;

    }
    //각 cell(날짜)의 가로길이
    private int getCellWidthDP()
    {
        int cellWidth = 480/7;
        return cellWidth;
    }
    //각 cell(날짜)의 여백길이
    private int getRestCellWidthDP()
    {
        int cellWidth = 480%7;
        return cellWidth;
    }
    //각 cell(날짜)의 세로길이
    private int getCellHeightDP()
    {
        int cellHeight = 960/6;
        return cellHeight;
    }

}
