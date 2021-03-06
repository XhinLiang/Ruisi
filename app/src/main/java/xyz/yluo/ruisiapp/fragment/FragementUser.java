package xyz.yluo.ruisiapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.yluo.ruisiapp.MySetting;
import xyz.yluo.ruisiapp.R;

/**
 * Created by free2 on 16-3-19.
 *
 */
public class FragementUser extends Fragment{

    @Bind(R.id.mytab)
    protected TabLayout mytab;
    private String uid = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        ButterKnife.bind(this, view);

        mytab.addTab(mytab.newTab().setText("回复我的"));
        mytab.addTab(mytab.newTab().setText("我的主题"));
        mytab.addTab(mytab.newTab().setText("私人消息"));
        mytab.addTab(mytab.newTab().setText("我的收藏"));
        uid = MySetting.CONFIG_USER_UID;



        mytab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //changeRecyclerViewData(mytab.getSelectedTabPosition());
                changeFrageMent(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        changeFrageMent(0);
        return view;
    }


    private void changeFrageMent(int position){

        //回复我的
        String url0 ="home.php?mod=space&do=notice";
        if(!MySetting.CONFIG_IS_INNER){
            url0+="&mobile=2";
        }
        //主题
        String url1 = "home.php?mod=space&uid="+uid+"&do=thread&view=me&mobile=2";
        //我的消息
        String url2 = "home.php?mod=space&do=pm&mobile=2";
        //我的收藏
        String url3 = "home.php?mod=space&uid="+uid+"&do=favorite&view=me&type=thread&mobile=2";
        Fragment fragment  = null;
        switch (position){
            case 0:
                fragment = FragementMessageReply.newInstance(0,url0);
                break;
            case 1:
                fragment= FragementArticlestar.newInstance(0,url1);
                break;
            case 2:
                fragment = FragementMessageReply.newInstance(1,url2);
                break;
            case 3:
                fragment= FragementArticlestar.newInstance(1,url3);
                break;
        }
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container_me, fragment);
        transaction.commit();
    }
}
