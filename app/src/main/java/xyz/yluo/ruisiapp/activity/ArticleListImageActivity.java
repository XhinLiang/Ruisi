package xyz.yluo.ruisiapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import xyz.yluo.ruisiapp.adapter.ArticleListImageAdapter;
import xyz.yluo.ruisiapp.data.ImageArticleListData;
import xyz.yluo.ruisiapp.httpUtil.HttpUtil;
import xyz.yluo.ruisiapp.httpUtil.ResponseHandler;
import xyz.yluo.ruisiapp.utils.UrlUtils;

/**
 * Created by free2 on 16-3-31.
 * 图片文章列表activity
 *
 */
public class ArticleListImageActivity extends ArticleListBaseActivity{

    private List<ImageArticleListData> mydatasetnormal;
    private ArticleListImageAdapter adapter;
    public static void open(Context context, int fid, String title){
        Intent intent = new Intent(context, ArticleListImageActivity.class);
        CurrentFid = fid;
        CurrentTitle = title;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar.setTitle(CurrentTitle);
        mydatasetnormal =  new ArrayList<>();
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new ArticleListImageAdapter(this,mydatasetnormal);
        mRecyclerView.setAdapter(adapter);

    }


    @Override
    protected void getData() {
        String url = UrlUtils.getArticleListUrl(CurrentFid,CurrentPage,true);
        HttpUtil.get(getApplicationContext(), url, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                new GetImageArticleListTaskRS(new String(response)).execute();
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(getApplicationContext(), "网络错误！！", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void refresh() {
    }

    @Override
    public void onLoadMore() {
    }

    //校园网状态下获得图片板块数据 图片链接、标题等  根据html获得数据
    public class GetImageArticleListTaskRS extends AsyncTask<Void, Void, String> {

        private String response;
        public GetImageArticleListTaskRS(String res) {
            this.response = res;
        }

        @Override
        protected String doInBackground(Void... params) {
            Elements list = Jsoup.parse(response).select("ul[id=waterfall]");
            Elements imagelist = list.select("li");

            for (Element tmp : imagelist) {
                //链接不带前缀
                //http://rs.xidian.edu.cn/
                String img = tmp.select("img").attr("src");
                String url = tmp.select("h3.xw0").select("a[href^=forum.php]").attr("href");
                String title = tmp.select("h3.xw0").select("a[href^=forum.php]").text();
                String author = tmp.select("a[href^=home.php]").text();
                String replyCount = tmp.select(".xg1.y").select("a[href^=forum.php]").text();
                tmp.select(".xg1.y").select("a[href^=forum.php]").remove();

                ImageArticleListData tem = new ImageArticleListData(title, url, img, author,replyCount);
                mydatasetnormal.add(tem);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String res) {
            refreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    }
}
