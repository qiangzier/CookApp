package com.hzq.cookapp.components.searchview;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hzq.cookapp.R;
import com.hzq.cookapp.utils.DensityUtil;

/**
 * @author: hezhiqiang
 * @date: 2017/7/21
 * @version:
 * @description:
 */

public class MaterialSearchView extends RelativeLayout{

    private ImageView imgBack;
    private ImageView imgCancel;
    private EditText editText;
    private View divider;
    private RecyclerView recyclerView;
    private RelativeLayout searchLayout;

    private OnQueryTextListener onQueryTextListener;
    private OnCloseListener onCloseListener;

    private int width,height;

    public MaterialSearchView(@NonNull Context context) {
        this(context,null);
    }

    public MaterialSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MaterialSearchView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        inflate(context,R.layout.searchview_layout,this);
        imgBack = (ImageView) findViewById(R.id.image_search_back);
        imgCancel = (ImageView) findViewById(R.id.clear_search);
        editText = (EditText) findViewById(R.id.edit_text_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        divider = findViewById(R.id.line_divider);
        searchLayout = (RelativeLayout) findViewById(R.id.search_layout);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imgCancel.setVisibility(TextUtils.isEmpty(s.toString()) ? GONE : VISIBLE);
                if(onQueryTextListener != null)
                    onQueryTextListener.onQueryTextChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //监听键盘搜索按钮
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
                    String keytag = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(keytag)) {
                        Toast.makeText(getContext(), "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    onQueryTextListener.onQueryTextSubmit(keytag);
                    hideSoftInput();
                    return true;
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                hideView();
                if(onCloseListener != null)
                    onCloseListener.onClose();
            }
        });

        imgCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

    }

    public boolean onBackPressed() {
        if(getVisibility() == VISIBLE){
            hideView();
            return true;
        }
        return false;
    }

    public void showView(){
        post(new Runnable() {
            @Override
            public void run() {
                showAnimation();
            }
        });
    }


    public void hideView(){
        post(new Runnable() {
            @Override
            public void run() {
                hideAnimation();
            }
        });
    }

    private Toolbar mToolbar;
    public void attchToolbar(Toolbar toolbar){
        mToolbar = toolbar;
    }

    private void showAnimation(){
        if(width == 0){
            if(mToolbar != null){
                width = mToolbar.getMeasuredWidth();
                height = mToolbar.getMeasuredHeight();
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Animator showAnimation = ViewAnimationUtils.createCircularReveal(this,
                    width - DensityUtil.dp2px(getContext(),56),
                    DensityUtil.dp2px(getContext(),23),
                    0,
                    (float) Math.hypot(width,height));
            showAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    showSoftInput();
                    editText.requestFocus();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            showAnimation.setDuration(300);
            showAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            showAnimation.start();
        }
    }

    private void hideAnimation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Animator hideAnimation = ViewAnimationUtils.createCircularReveal(this,
                    width - DensityUtil.dp2px(getContext(),56),
                    DensityUtil.dp2px(getContext(),23),
                    (float) Math.hypot(width,height),
                    0);
            hideAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setVisibility(GONE);
                    hideSoftInput();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            hideAnimation.setDuration(300);
            hideAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            hideAnimation.start();
        }
    }

    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    public void showSoftInput(){
//        InputMethodManager inputManager = (InputMethodManager) getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(view, 0);

        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
        this.onQueryTextListener = onQueryTextListener;
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public interface OnCloseListener {
        boolean onClose();
    }

    private void hideDivider(){
        if(divider != null)
            divider.setVisibility(GONE);
    }

    private void showDivider(){
        if(divider != null)
            divider.setVisibility(VISIBLE);
    }

}
