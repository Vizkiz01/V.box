package com.vbox.movies;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.View;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.graphics.Typeface;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(5, 8, 18));
        getWindow().setNavigationBarColor(Color.rgb(5, 8, 18));
        setContentView(new IntroView());
    }

    class IntroView extends View {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        float progress = 0f;

        IntroView() {
            super(MainActivity.this);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            setBackgroundColor(Color.rgb(4, 6, 14));

            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(2400);
            animator.setInterpolator(
                new AccelerateDecelerateInterpolator()
            );
            animator.addUpdateListener(a -> {
                progress = (float) a.getAnimatedValue();
                invalidate();
            });
            animator.start();
        }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);

            float w = getWidth();
            float h = getHeight();
            float cx = w / 2f;
            float cy = h / 2f;

            // Cinematic blue glow
            p.setShader(new RadialGradient(
                cx, cy + 50, w * 0.65f,
                new int[]{
                    Color.rgb(0, 65, 150),
                    Color.rgb(4, 15, 38),
                    Color.rgb(4, 6, 14)
                },
                null, Shader.TileMode.CLAMP
            ));
            c.drawRect(0, 0, w, h, p);
            p.setShader(null);

            // Timeline for the animation
            float reelX;
            float reelY = cy + 35;
            float reelSize = Math.min(w, h) * 0.105f;
            float rotation;
            float tapeAlpha;
            float logoAlpha;

            if (progress < 0.35f) {
                float t = progress / 0.35f;
                reelX = -reelSize + t * (cx * 0.65f + reelSize);
                rotation = t * 540f;
                tapeAlpha = 1f;
                logoAlpha = 0f;
            } else if (progress < 0.60f) {
                float t = (progress - 0.35f) / 0.25f;
                reelX = cx * 0.65f + reelSize
                    + (float)Math.sin(t * Math.PI) * 12;
                reelY = cy + 35 + (float)Math.sin(t * Math.PI) * 22;
                rotation = 540f + t * 160f;
                tapeAlpha = 1f;
                logoAlpha = 0f;
            } else {
                float t = Math.min(1f, (progress - 0.60f) / 0.22f);
                reelX = cx;
                reelY = cy + 35;
                rotation = 700f;
                tapeAlpha = 1f - t;
                logoAlpha = Math.min(1f, (progress - 0.72f) / 0.18f);
            }

            // Floor light
            p.setColor(Color.rgb(0, 95, 255));
            p.setStrokeWidth(3);
            c.drawLine(
                cx - w * 0.36f, cy + 110,
                cx + w * 0.36f, cy + 110, p
            );

            // Draw the reel
            if (progress < 0.82f) {
                c.save();
                c.translate(reelX, reelY);
                c.rotate(rotation);

                p.setColor(Color.rgb(155, 185, 220));
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(5);
                c.drawCircle(0, 0, reelSize, p);

                p.setColor(Color.rgb(40, 75, 120));
                p.setStrokeWidth(3);
                c.drawCircle(0, 0, reelSize * 0.72f, p);

                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.rgb(190, 215, 245));
                c.drawCircle(0, 0, reelSize * 0.18f, p);

                for (int i = 0; i < 5; i++) {
                    double angle = i * Math.PI * 2 / 5;
                    float x = (float)Math.cos(angle) * reelSize * 0.48f;
                    float y = (float)Math.sin(angle) * reelSize * 0.48f;
                    p.setColor(Color.rgb(4, 8, 18));
                    c.drawCircle(x, y, reelSize * 0.16f, p);
                }

                c.restore();
            }

            // Film strip springs up into a V
            if (progress >= 0.55f) {
                float t = Math.min(1f, (progress - 0.55f) / 0.28f);
                float bounce = (float)Math.sin(t * Math.PI);
                float vSize = Math.min(w, h) * 0.28f;

                c.save();
                c.translate(cx, cy + 10 - bounce * 30);

                Path v = new Path();
                v.moveTo(-vSize * 0.58f, -vSize * 0.72f);
                v.lineTo(-vSize * 0.10f, vSize * 0.62f);
                v.lineTo(vSize * 0.58f, -vSize * 0.72f);

                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(vSize * 0.17f);
                p.setStrokeCap(Paint.Cap.SQUARE);
                p.setColor(Color.rgb(25, 110, 255));
                p.setAlpha((int)(255 * tapeAlpha));
                c.drawPath(v, p);

                p.setColor(Color.rgb(220, 235, 255));
                p.setStrokeWidth(vSize * 0.115f);
                c.drawPath(v, p);

                // Film perforations
                p.setColor(Color.rgb(5, 12, 28));
                p.setStrokeWidth(vSize * 0.025f);
                c.drawPath(v, p);

                p.setStyle(Paint.Style.FILL);
                p.setAlpha(255);
                c.restore();
            }

            // V.BOX logo reveal
            if (progress >= 0.72f) {
                p.setAlpha((int)(255 * logoAlpha));
                p.setColor(Color.WHITE);
                p.setTextAlign(Paint.Align.CENTER);
                p.setTypeface(Typeface.create(
                    "sans-serif", Typeface.BOLD
                ));
                p.setTextSize(Math.min(w * 0.095f, 54));
                c.drawText("V.BOX", cx, cy + h * 0.27f, p);

                p.setTypeface(Typeface.create(
                    "sans-serif-light", Typeface.NORMAL
                ));
                p.setTextSize(Math.min(w * 0.033f, 18));
                p.setColor(Color.rgb(150, 195, 255));
                c.drawText(
                    "YOUR MOVIE UNIVERSE",
                    cx, cy + h * 0.32f, p
                );
                p.setAlpha(255);
            }

            // Small cinematic light particles
            if (progress >= 0.55f && progress < 0.9f) {
                float t = (progress - 0.55f) / 0.35f;
                p.setColor(Color.rgb(40, 145, 255));

                for (int i = 0; i < 16; i++) {
                    float angle = i * 0.3927f;
                    float distance = t * (40 + i * 5);
                    float x = cx + (float)Math.cos(angle) * distance;
                    float y = cy + (float)Math.sin(angle) * distance;
                    c.drawCircle(x, y, 2.5f * (1f - t), p);
                }
            }
        }
    }
}
