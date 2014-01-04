/*
 * Copyright (C) 2013 FMSoft (http://www.fmsoft.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.espier.ios6.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.logistics.R;

/**
 * IPhone like password lock view. 4 number password.
 * @author mingming
 */
public class IPhoneLockPasswordView extends View {
    public static final int MSG_PASSWORD_DONE = 100;
    public static final int DELAY_TIME = 100;

    // UI cell id defined
    private static final int ID_BASE = 0;
    private static final int ID_NUMBER_0 = 0; //ID_BASE + 1;
    /*private static final int ID_NUMBER_1 = ID_BASE + 2;
    private static final int ID_NUMBER_2 = ID_BASE + 3;
    private static final int ID_NUMBER_3 = ID_BASE + 4;
    private static final int ID_NUMBER_4 = ID_BASE + 5;
    private static final int ID_NUMBER_5 = ID_BASE + 6;
    private static final int ID_NUMBER_6 = ID_BASE + 7;
    private static final int ID_NUMBER_7 = ID_BASE + 8;
    private static final int ID_NUMBER_8 = ID_BASE + 9;
    private static final int ID_NUMBER_9 = ID_BASE + 10;*/

    private static final int ID_DISPLAY_1 = 0; //ID_BASE + 11;
    private static final int ID_DISPLAY_2 = 1; //ID_BASE + 12;
    private static final int ID_DISPLAY_3 = 2; //ID_BASE + 13;
    private static final int ID_DISPLAY_4 = 3; //ID_BASE + 14;

    private static final int ID_BUTTON_BACK = ID_BASE + 15;

    // number cell data
    private static final int CELL_DISPLAY = -1;
    private static final int CELL_NUMBER_0 = 0;
    private static final int CELL_NUMBER_1 = 1;
    private static final int CELL_NUMBER_2 = 2;
    private static final int CELL_NUMBER_3 = 3;
    private static final int CELL_NUMBER_4 = 4;
    private static final int CELL_NUMBER_5 = 5;
    private static final int CELL_NUMBER_6 = 6;
    private static final int CELL_NUMBER_7 = 7;
    private static final int CELL_NUMBER_8 = 8;
    private static final int CELL_NUMBER_9 = 9;
    private static final int CELL_KEY_BACK = 10;

    private static final int CANCLE_INPUT = -9999;

    private Cell mFocusCell;
    private NumberCell mNumberCells[];
    private DisplayCell mDisplayCells[];
    private KeyCell mKeyCellBack;

    private Context mContext = null;
    private ArrayList<Cell> mChildList;

    private InputState mInputState;
    private OnPasswordLister mPasswordLister;

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int mCellWidth;
    private int mCellHeight;

    // UI item value
    private int mFrameworkColor;
    private int mGridColor;
    private int mDisplayBkColor;
    private int mDisplayInputColor;
    private int mNumberColor;
    private int mFocusColor;

    private float mFrameworkWidth;
    private float mGridWidth;
    private float mDisplayScale;
    private float mDisplayNumberScale;
    private float mNumberScale;
    private float mRoundRx;
    private float mRoundRy;
    private float mBackAspect;
    private float mBackScale;
    private float mBackItemScale;
    private float mBackWidth;

    enum InputState {
        NotInput,
        Input_1,
        Input_2,
        Input_3,
        InputDone,
    }

    public interface OnPasswordLister {
        void onPasswordDone(String password);
    }


    public IPhoneLockPasswordView(Context context) {
        super(context);

        initView(context);
    }

    public IPhoneLockPasswordView(Context context, AttributeSet attr) {
        super(context, attr);

        initView(context);
    }

    /*public IPhoneLockPasswordLayout(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        // TODO Auto-generated constructor stub
        initView(context);
    }*/

    private void initView(Context context) {
        int i;
        float base = 100.0f;
        Resources res;

        mContext = context;
        mChildList = new ArrayList<Cell> ();

        // create display cell
        mDisplayCells = new DisplayCell[4];
        for (i = 0; i < 4; i++) {
            mDisplayCells[i] = new DisplayCell(ID_DISPLAY_1 + i, CELL_DISPLAY, i, false);
            addChildCell(mDisplayCells[i]);
        }

        // create back key cell
        mKeyCellBack = new KeyCell(ID_BUTTON_BACK, CELL_KEY_BACK, 0, true);
        addChildCell(mKeyCellBack);

        // create number cell
        mNumberCells = new NumberCell[10];
        for (i = CELL_NUMBER_0; i < CELL_NUMBER_9 + 1; i++) {
            mNumberCells[i] = new NumberCell(ID_NUMBER_0 + i, i, i, true);
            addChildCell(mNumberCells[i]);
        }

        mPaint = new Paint();
        res = mContext.getResources();

        mFrameworkColor = res.getColor(R.color.lock_password_framework_color);
        mGridColor = res.getColor(R.color.lock_password_grid_color);
        mDisplayBkColor = res.getColor(R.color.lock_password_display_bk_color);
        mDisplayInputColor = res.getColor(R.color.lock_password_display_input_color);
        mNumberColor = res.getColor(R.color.lock_password_number_color);
        mFocusColor = res.getColor(R.color.lock_password_focus_color);

        base = res.getInteger(R.integer.lock_password_base);
        mFrameworkWidth = res.getInteger(R.integer.lock_password_framework_width) / base;
        mGridWidth = res.getInteger(R.integer.lock_password_grid_width) / base;
        mDisplayScale = res.getInteger(R.integer.lock_password_display_scale) / base;
        mDisplayNumberScale = res.getInteger(R.integer.lock_password_display_number_scale) / base;
        mNumberScale = res.getInteger(R.integer.lock_password_number_scale) / base;
        mRoundRx = res.getInteger(R.integer.lock_password_round_radius_rx) / base;
        mRoundRy = res.getInteger(R.integer.lock_password_round_radius_ry) / base;
        mBackAspect = res.getInteger(R.integer.lock_password_back_aspect) / base;
        mBackScale = res.getInteger(R.integer.lock_password_back_scale) / base;
        mBackItemScale = res.getInteger(R.integer.lock_password_back_item_scale) / base;
        mBackWidth = res.getInteger(R.integer.lock_password_back_width) / base;

        mInputState = InputState.NotInput;
        mPasswordLister = null;
        mFocusCell = null;

        mWidth = getWidth();
        mHeight = getHeight();

        mCellWidth = mWidth / 5;
        mCellHeight = mHeight / 3;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int i;
        int dx, dy;
        int displayWidth;
        int displayHeight;
        int numberWidth;
        int numberHeight;

        mWidth = w;
        mHeight = h;

        // 3 cell in one line
        mCellHeight = h / 3;

        // 4 display cell and 1 back key cell in one row
        mCellWidth = w / 5;
        displayWidth = (int) (mCellWidth * mDisplayScale);
        displayHeight = (int) (mCellHeight * mDisplayScale);

        dx = (mCellWidth - displayWidth) / 2;
        dy = (mCellHeight - displayHeight) / 2;

        // 5 number cell in one row
        numberWidth = w / 5;
        numberHeight = mCellHeight;

        // set display cell rect
        for (i = 0; i < mDisplayCells.length; i++) {
            mDisplayCells[i].mRect.left = dx + (displayWidth * i) + (dx * 2 * i);
            mDisplayCells[i].mRect.top = dy;
            mDisplayCells[i].mRect.right = mDisplayCells[i].mRect.left + displayWidth;
            mDisplayCells[i].mRect.bottom = mDisplayCells[i].mRect.top + displayHeight;
        }

        // set back key cell rect
        mKeyCellBack.mRect.left = dx + (displayWidth * i) + (dx * 2 * i);
        mKeyCellBack.mRect.top = dy;
        mKeyCellBack.mRect.right = mKeyCellBack.mRect.left + displayWidth;
        mKeyCellBack.mRect.bottom = mKeyCellBack.mRect.top + displayHeight;

        // set number cell rect, 5 cell in one row
        for (i = 0; i < mNumberCells.length; i++) {
            mNumberCells[i].mRect.left = (numberWidth * (i % 5));
            mNumberCells[i].mRect.top = mCellHeight * (1 +(i / 5));
            mNumberCells[i].mRect.right = mNumberCells[i].mRect.left + numberWidth;
            mNumberCells[i].mRect.bottom = mNumberCells[i].mRect.top + numberHeight;
        }
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.UNSPECIFIED
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            throw new RuntimeException("CellLayout cannot have UNSPECIFIED dimensions");
        }

        int i;
        int childCount;
        int cellWidth;
        int cellHeight;
        int orientation;
        LayoutParams lp;
        View childView;

        childCount = getChildCount();
        orientation = getOrientation();

        for (i = 0; i < childCount; i++) {
            childView = (View) getChildAt(i);
            if (null == childView) {
                continue;
            }

            if (HORIZONTAL == orientation) {
                cellWidth = widthSpecSize / childCount;
                cellHeight = heightSpecSize;
            } else {
                cellWidth = widthSpecSize;
                cellHeight = heightSpecSize / childCount;
            }

               lp = (LayoutParams) childView.getLayoutParams();
               lp.width = cellWidth;
               lp.height = cellHeight;

               int childWidthMeasureSpec =
                   MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
               int childheightMeasureSpec =
                   MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
               childView.measure(childWidthMeasureSpec, childheightMeasureSpec);
        }

        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }*/

    @Override
	public boolean dispatchTouchEvent(MotionEvent event) {
        Cell childCell = null;
        Cell targetCell = null;
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = (event.getAction() & MotionEvent.ACTION_MASK);


        for (int i = 0; i < mChildList.size(); i++) {
            childCell = mChildList.get(i);

            // we just let one cell in touch
            if (childCell.getRect().contains(x, y)) {
                //childCell.onTouchEvent(event);
                targetCell = childCell;
                break;
            }
        }

        // lock one pressed target cell
        if (null == mFocusCell) {
            switch(action) {
            case MotionEvent.ACTION_DOWN:
                mFocusCell = targetCell;
                break;

            default:
                break;
            }
        } else {
            switch(action) {
            case MotionEvent.ACTION_UP:
                targetCell = mFocusCell;
                mFocusCell = null;
                break;

            case MotionEvent.ACTION_MOVE:
                targetCell = mFocusCell;
                break;

            default:
                break;
            }
        }

        if (null != targetCell) {
            targetCell.onTouchEvent(event);
        }

        // return true means I want to receive the touch event
        //return super.dispatchTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int i;
        float startX, startY;
        float endX, endY;
        RectF rectF = new RectF();

        mPaint.setAntiAlias(true);

        // draw child cell
        for (i = 0; i < mChildList.size(); i++) {
            mChildList.get(i).onDraw(canvas);
        }

        // draw grid
        mPaint.setStrokeWidth(mGridWidth);
        mPaint.setColor(mGridColor);

        for (i = 1; i < 3; i++) {
            startX = 0;
            startY = mCellHeight * i;
            endX = mWidth;
            endY = startY;
            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }

        for (i = 1; i < 5; i++) {
            startX = mCellWidth * i;
            startY = mCellHeight * 1;
            endX = startX;
            endY = mHeight;
            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }

        // draw rectangle framework
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mFrameworkWidth);
        mPaint.setColor(mFrameworkColor);

        rectF.left = 0.0f;
        rectF.top = 0.0f;
        rectF.right = mWidth;
        rectF.bottom = mHeight;

        canvas.drawRoundRect(rectF, mRoundRx, mRoundRy, mPaint);
    }

    public void setOnPasswordLister(OnPasswordLister lister) {
        mPasswordLister = lister;
    }

    public void resetInput() {
        for (int i = 0; i < mDisplayCells.length; i++) {
            mDisplayCells[i].setInputedValue(CANCLE_INPUT);
        }
        mInputState = InputState.NotInput;
    }

    private void addChildCell(Cell child) {
        if (null == mChildList) {
            mChildList = new ArrayList<Cell> ();
        }

        mChildList.add(child);
        child.setParent(this);
    }

    private void onCellClick(Cell cell) {
        int value = cell.getValue();
        int engine = cell.getCellValue();

        if (InputState.NotInput == mInputState) {
            if (isInputNumber(engine)) {
                // switch state
                mInputState = InputState.Input_1;

                // on move to state Input_1
                mDisplayCells[ID_DISPLAY_1].setInputedValue(value);
            }

        } else if (InputState.Input_1 == mInputState) {
            if (isInputNumber(engine)) {
                mInputState = InputState.Input_2;

                mDisplayCells[ID_DISPLAY_2].setInputedValue(value);
            } else if (CELL_KEY_BACK == engine) {
                mInputState = InputState.NotInput;

                mDisplayCells[ID_DISPLAY_1].setInputedValue(CANCLE_INPUT);
            }

        } else if (InputState.Input_2 == mInputState) {
            if (isInputNumber(engine)) {
                mInputState = InputState.Input_3;

                mDisplayCells[ID_DISPLAY_3].setInputedValue(value);
            } else if (CELL_KEY_BACK == engine) {
                mInputState = InputState.Input_1;

                mDisplayCells[ID_DISPLAY_2].setInputedValue(CANCLE_INPUT);
            }

        } else if (InputState.Input_3 == mInputState) {
            if (isInputNumber(engine)) {
                //mInputState = InputState.NotInput;
                mInputState = InputState.InputDone;

                mDisplayCells[ID_DISPLAY_4].setInputedValue(value);

                // password input done call the callback
                if (null != mPasswordLister) {
                    mPasswordLister.onPasswordDone(String.format("%d%d%d%d",
                            mDisplayCells[ID_DISPLAY_1].getValue(),
                            mDisplayCells[ID_DISPLAY_2].getValue(),
                            mDisplayCells[ID_DISPLAY_3].getValue(),
                            mDisplayCells[ID_DISPLAY_4].getValue()));
                }
            } else if (CELL_KEY_BACK == engine) {
                mInputState = InputState.Input_2;

                mDisplayCells[ID_DISPLAY_3].setInputedValue(CANCLE_INPUT);
            }

        } else if (InputState.InputDone == mInputState) {
            // do noting
        }
    }

    private boolean isInputNumber(int cellValue) {
        boolean ret = false;
        if (CELL_NUMBER_0 == cellValue ||
                CELL_NUMBER_1 == cellValue ||
                CELL_NUMBER_2 == cellValue ||
                CELL_NUMBER_3 == cellValue ||
                CELL_NUMBER_4 == cellValue ||
                CELL_NUMBER_5 == cellValue ||
                CELL_NUMBER_6 == cellValue ||
                CELL_NUMBER_7 == cellValue ||
                CELL_NUMBER_8 == cellValue ||
                CELL_NUMBER_9 == cellValue) {
            ret = true;
        }
        return ret;
    }


    /**
     * ************************************************
     *  lock password UI element cell class
     * ************************************************
    */
    abstract class Cell {
        int mID;
        int mValue;
        int mCellValue;

        boolean mPressed;
        boolean mInputed;
        boolean mInputable;

        Rect mRect;
        View mParent;

        Cell() {
            mID = ID_BASE;
            mRect = new Rect();
        }

        Cell(int id, int cellValue, int value, boolean inputable) {
            mID = id;
            mCellValue = cellValue;
            mValue = value;
            mInputable = inputable;
            mRect = new Rect();
        }

        abstract void onDraw(Canvas canvas);

        void setParent(View parent) {
            mParent = parent;
        }

        void setInputedValue(int value) {
            if (mInputable) {
                return;
            }

            if (CANCLE_INPUT == value) {
                mInputed = false;
            } else {
                mInputed = true;
            }

            mValue = value;
            mParent.invalidate(mRect);
        }

        Rect getRect() {
            return mRect;
        }

        int getCellValue() {
            return mCellValue;
        }

        int getValue() {
            return mValue;
        }

        void onTouchEvent(MotionEvent event) {
            if (!mInputable) {
                return;
            }

            switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPressed = true;
                mParent.invalidate(mRect);
                break;

            case MotionEvent.ACTION_UP:
                mPressed = false;
                onCellClick(this);
                mParent.invalidate(mRect);
                break;

            case MotionEvent.ACTION_CANCEL:
                mPressed = false;
                mParent.invalidate(mRect);
                break;

            default:
                break;
            }
        }
    }


    class NumberCell extends Cell {

        public NumberCell() {
            super();
        }

        public NumberCell(int id, int cellValue, int value, boolean inputable) {
            super(id, cellValue, value, inputable);
        }

        @Override
        void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            String text;
            Rect rect = new Rect();

            // just for test
            //int color;
            //color = 0xff0000ff + mValue * 1000;
            //mPaint.setStyle(Paint.Style.FILL);
            //mPaint.setColor(color);
            //canvas.drawRect(mRect, mPaint);

            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize((mRect.width()) * mNumberScale);

            text = String.format("%d", mValue);
            mPaint.getTextBounds(text, 0, text.length(), rect);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mNumberColor);
            canvas.drawText(String.format("%d", mValue),
                    mRect.left + mRect.width() / 2,
                    mRect.top + ((mRect.height() / 2) + (rect.height() / 2)),
                    mPaint);

            if (mPressed) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mFocusColor);
                canvas.drawRect(mRect, mPaint);
            }
        }
    }


    class DisplayCell extends Cell {

        public DisplayCell() {
            super();
        }

        public DisplayCell(int id, int cellValue, int value, boolean inputable) {
            super(id, cellValue, value, inputable);
        }

        @Override
        void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            // draw background
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mDisplayBkColor);
            canvas.drawRect(mRect, mPaint);

            // draw input number
            if (mInputed) {
                int square;
                RectF rectF = new RectF();

                if (mRect.width() <= mRect.height()) {
                    square = (int) (mRect.width() * mDisplayNumberScale);
                } else {
                    square = (int) (mRect.height() * mDisplayNumberScale);
                }

                rectF.left = mRect.left + (mRect.width() - square) / 2;
                rectF.top = mRect.top + (mRect.height() - square) / 2;
                rectF.right = rectF.left + square;
                rectF.bottom = rectF.top + square;

                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mDisplayInputColor);
                canvas.drawArc(rectF, 0, 360, true, mPaint);
            }
        }
    }


    class KeyCell extends Cell {

        public KeyCell() {
            super();
        }

        public KeyCell(int id, int cellValue, int value, boolean inputable) {
            super(id, cellValue, value, inputable);
        }

        @Override
        void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            float w, h;
            float arrowW, rectW;
            float nextX, nextY;
            Path path = new Path();

            w = (mRect.width() * mBackScale);
            h = Math.min(mRect.height() * mBackScale, w / mBackAspect);

            arrowW = w / (1 + mBackAspect);
            rectW = w - arrowW;

            // create the arrow path
            nextX = mRect.left + (mRect.width() - w) / 2;
            nextY = mRect.top + mRect.height() / 2;
            path.moveTo(nextX, nextY);

            nextX = nextX + arrowW;
            nextY = nextY - h / 2;
            path.lineTo(nextX, nextY);

            nextX = nextX + rectW;
            path.lineTo(nextX, nextY);

            nextY = nextY + h;
            path.lineTo(nextX, nextY);

            nextX = nextX - rectW;
            path.lineTo(nextX, nextY);

            nextX = nextX - arrowW;
            nextY = nextY - h / 2;
            path.lineTo(nextX, nextY);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mNumberColor);
            canvas.drawPath(path, mPaint);

            // draw "X"
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mDisplayInputColor);
            mPaint.setStrokeWidth(mBackWidth);

            nextX = nextX + arrowW + ((rectW * (1 - mBackItemScale)) / 2);
            nextY = mRect.top + (mRect.height() - (h * mBackItemScale)) / 2;

            canvas.drawLine(nextX, nextY,
                    nextX + (rectW * mBackItemScale), nextY + (h * mBackItemScale),
                    mPaint);

            canvas.drawLine(nextX, nextY + (h * mBackItemScale),
                    nextX + (rectW * mBackItemScale), nextY,
                    mPaint);

            if (mPressed) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mFocusColor);
                canvas.drawPath(path, mPaint);
            }
        }
    }
    /**
     * ************************************************
     *  end lock password UI element cell class
     * ************************************************
    */
}
