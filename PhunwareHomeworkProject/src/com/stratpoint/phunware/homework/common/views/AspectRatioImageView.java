package com.stratpoint.phunware.homework.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.stratpoint.phunware.homework.R;
 
/*
 * An ImageView that adjusts its height according
 * to a given ratio. This is useful for dynamic layouts where
 * the ImageView's height must adjust accordingly when its width
 * changes, say, for instances where its width is fill_parent. 
 */
public class AspectRatioImageView extends ImageView {
	
	private float aspectRatio;
 
	public AspectRatioImageView(Context context) {
		this(context, null);
	}
 
	public AspectRatioImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
 
	public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		int horizontalRatio = 0, verticalRatio = 0;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView, 0, 0);
		for (int x = 0; x < typedArray.length(); x++) {
			int index = typedArray.getIndex(x); 
			switch (index) {
				case R.styleable.AspectRatioImageView_aspectRatioHorizontal:
					horizontalRatio = typedArray.getInteger(index, 0);
					break;
				case R.styleable.AspectRatioImageView_aspectRatioVertical:
					verticalRatio = typedArray.getInteger(index, 0);
					break;
				case R.styleable.AspectRatioImageView_aspectRatio:
					aspectRatio = typedArray.getFloat(index, 0);
					break;
			}
		}
		typedArray.recycle();
		
		aspectRatio = horizontalRatio > 0 && verticalRatio > 0 ? (float)horizontalRatio/verticalRatio : aspectRatio;
	}
	
	public void setAspectRatio(float aspectRatio){
		this.aspectRatio = aspectRatio;
	}
	
	public void setAspectRatio(int widthRatio, int heightRatio){
		this.aspectRatio = (float) widthRatio / heightRatio;
	}
 
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (aspectRatio == 0)
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		else {
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = (int) (MeasureSpec.getSize(widthMeasureSpec) / aspectRatio);
			setMeasuredDimension(width, height);
		}
	}
}