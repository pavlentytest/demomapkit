package com.example.mapkittest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Circle;
import com.yandex.mapkit.geometry.LinearRing;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polygon;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CircleMapObject;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolygonMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.AnimatedImageProvider;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.ui_view.ViewProvider;

import java.util.ArrayList;
import java.util.Random;

/**
 * This example shows how to add simple objects such as polygons, circles and polylines to the map.
 * It also shows how to display images instead.
 */
public class MainActivity extends Activity {
    /**
     * Replace "your_api_key" with a valid developer key.
     * You can get it at the https://developer.tech.yandex.ru/ website.
     */
    private final String MAPKIT_API_KEY = "123123";
    private final Point CAMERA_TARGET = new Point(55.554047, 37.421903);
    private final Point ANIMATED_RECTANGLE_CENTER = new Point(55.552831, 37.418256);
    private final Point TRIANGLE_CENTER = new Point(55.554668, 37.425251);
    private final Point POLYLINE_CENTER = CAMERA_TARGET;
    private final Point CIRCLE_CENTER = new Point(55.552648, 37.424371);
    private final Point DRAGGABLE_PLACEMARK_CENTER = new Point(55.556063, 37.417625);
    private final double OBJECT_SIZE = 0.0015;

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private Handler animationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mapView = (MapView)findViewById(R.id.mapview);
        mapView.getMap().move(
                new CameraPosition(CAMERA_TARGET, 14.0f, 0.0f, 0.0f));
        mapObjects = mapView.getMap().getMapObjects().addCollection();
        animationHandler = new Handler();
        createMapObjects();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    private void createMapObjects() {
        AnimatedImageProvider animatedImage = AnimatedImageProvider.fromAsset(this, "animation.png");
        ArrayList<Point> rectPoints = new ArrayList<>();
        rectPoints.add(new Point(
                ANIMATED_RECTANGLE_CENTER.getLatitude() - OBJECT_SIZE,
                ANIMATED_RECTANGLE_CENTER.getLongitude() - OBJECT_SIZE));
        rectPoints.add(new Point(
                ANIMATED_RECTANGLE_CENTER.getLatitude() - OBJECT_SIZE,
                ANIMATED_RECTANGLE_CENTER.getLongitude() + OBJECT_SIZE));
        rectPoints.add(new Point(
                ANIMATED_RECTANGLE_CENTER.getLatitude() + OBJECT_SIZE,
                ANIMATED_RECTANGLE_CENTER.getLongitude() + OBJECT_SIZE));
        rectPoints.add(new Point(
                ANIMATED_RECTANGLE_CENTER.getLatitude() + OBJECT_SIZE,
                ANIMATED_RECTANGLE_CENTER.getLongitude() - OBJECT_SIZE));
        PolygonMapObject rect = mapObjects.addPolygon(
                new Polygon(new LinearRing(rectPoints), new ArrayList<LinearRing>()));
        rect.setStrokeColor(Color.TRANSPARENT);
        rect.setFillColor(Color.TRANSPARENT);
        rect.setAnimatedImage(animatedImage, 32.0f);

      /*  ArrayList<Point> trianglePoints = new ArrayList<>();
        trianglePoints.add(new Point(
                TRIANGLE_CENTER.getLatitude() + OBJECT_SIZE,
                TRIANGLE_CENTER.getLongitude() - OBJECT_SIZE));
        trianglePoints.add(new Point(
                TRIANGLE_CENTER.getLatitude() - OBJECT_SIZE,
                TRIANGLE_CENTER.getLongitude() - OBJECT_SIZE));
        trianglePoints.add(new Point(
                TRIANGLE_CENTER.getLatitude(),
                TRIANGLE_CENTER.getLongitude() + OBJECT_SIZE));
        PolygonMapObject triangle = mapObjects.addPolygon(
                new Polygon(new LinearRing(trianglePoints), new ArrayList<LinearRing>()));
        triangle.setFillColor(Color.BLUE);
        triangle.setStrokeColor(Color.BLACK);
        triangle.setStrokeWidth(1.0f);
        triangle.setZIndex(100.0f);*/

        createTappableCircle(CIRCLE_CENTER);

     /*   ArrayList<Point> polylinePoints = new ArrayList<>();
        polylinePoints.add(new Point(
                POLYLINE_CENTER.getLatitude() + OBJECT_SIZE,
                POLYLINE_CENTER.getLongitude()- OBJECT_SIZE));
        polylinePoints.add(new Point(
                POLYLINE_CENTER.getLatitude() - OBJECT_SIZE,
                POLYLINE_CENTER.getLongitude()- OBJECT_SIZE));
        polylinePoints.add(new Point(
                POLYLINE_CENTER.getLatitude(),
                POLYLINE_CENTER.getLongitude() + OBJECT_SIZE));

        PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(polylinePoints));
        polyline.setStrokeColor(Color.BLACK);
        polyline.setZIndex(100.0f);
*/
      /*  PlacemarkMapObject mark = mapObjects.addPlacemark(DRAGGABLE_PLACEMARK_CENTER);
        mark.setOpacity(0.5f);
        mark.setIcon(ImageProvider.fromResource(this, R.drawable.pointer));
        mark.setDraggable(true);
        mark.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                Intent intent = new Intent(getApplicationContext(), Ber.class);
                startActivity(intent);
                return true;
            }
        });
*/
        createPlacemarkMapObjectWithViewProvider();
    }

    // Strong reference to the listener.
    private MapObjectTapListener circleMapObjectTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(MapObject mapObject, Point point) {
            if (mapObject instanceof CircleMapObject) {
                CircleMapObject circle = (CircleMapObject)mapObject;
                Object userData = circle.getUserData();
                if (userData instanceof CircleMapObjectUserData) {
                    CircleMapObjectUserData circleUserData = (CircleMapObjectUserData)userData;

                    Toast toast = Toast.makeText(
                            getApplicationContext(),
                            "Circle with id " + circleUserData.id + " and description '"
                                    + circleUserData.description + "' tapped",
                            Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent;
                    if(circleUserData.id == 0) {
                        intent = new Intent(getApplicationContext(), Ber.class);
                        Log.d("RRR",circleUserData.id+"");
                    } else {
                        intent = new Intent(getApplicationContext(), Ber.class);
                        Log.d("RRR",circleUserData.id+"");
                    }
                    startActivity(intent);
                }
            }
            return true;
        }
    };



    private class CircleMapObjectUserData {
        final int id;
        final String description;

        CircleMapObjectUserData(int id, String description) {
            this.id = id;
            this.description = description;
        }
    }

    private void createTappableCircle(Point center) {

        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(55.552648, 37.424371));
        points.add(new Point(55.552912, 37.423415));
        points.add(new Point(55.552818, 37.424401));


        for (int i = 0; i<points.size(); i++) {

            CircleMapObject circle = mapObjects.addCircle(
                new Circle(points.get(i), 10), Color.GREEN, 2, Color.RED);
        circle.setZIndex(10.0f);
        circle.setUserData(new CircleMapObjectUserData(i, "Tappable circle"));

        // Client code must retain strong reference to the listener.
        circle.addTapListener(circleMapObjectTapListener);

        }
    }

    private void createPlacemarkMapObjectWithViewProvider() {
        final TextView textView = new TextView(this);
        final int[] colors = new int[] { Color.RED, Color.GREEN, Color.BLACK };
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);

        textView.setTextColor(Color.RED);
        textView.setText("Hello, World!");

        final ViewProvider viewProvider = new ViewProvider(textView);
        final PlacemarkMapObject viewPlacemark =
                mapObjects.addPlacemark(new Point(55.554522, 37.418620), viewProvider);

        final Random random = new Random();
        final int delayToShowInitialText = 5000;  // milliseconds
        final int delayToShowRandomText = 500; // milliseconds;

        // Show initial text `delayToShowInitialText` milliseconds and then
        // randomly change text in textView every `delayToShowRandomText` milliseconds
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final int randomInt = random.nextInt(1000);
                textView.setText("Some text version " + randomInt);
                textView.setTextColor(colors[randomInt % colors.length]);
                viewProvider.snapshot();
                viewPlacemark.setView(viewProvider);
                animationHandler.postDelayed(this, delayToShowRandomText);
            }
        }, delayToShowInitialText);
    }
}