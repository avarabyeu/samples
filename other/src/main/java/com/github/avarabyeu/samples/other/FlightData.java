package com.github.avarabyeu.samples.other;

import java.util.Date;

/**
 * Created by andrey.vorobyov on 3/24/15.
 */
public class FlightData {

    private String time;
    private String mode_s;
    private String callsign;
    private Float latitude;
    private Float longitude;
    private String position;
    private Long altitude_feet;
    private Long squawk;
    private Long ground_speed;
    private Long ignore;
    private Long vspeed;
    private String on_ground;
    private Long radar_id;


    public FlightData(String time, String mode_s, String callsign, Float latitude, Float longitude, String position, Long altitude_feet, Long squawk, Long ground_speed, Long ignore, Long vspeed, String on_ground, Long radar_id) {
        this.time = time;
        this.mode_s = mode_s;
        this.callsign = callsign;
        this.latitude = latitude;
        this.longitude = longitude;
        this.position = position;
        this.altitude_feet = altitude_feet;
        this.squawk = squawk;
        this.ground_speed = ground_speed;
        this.ignore = ignore;
        this.vspeed = vspeed;
        this.on_ground = on_ground;
        this.radar_id = radar_id;
    }

    public String getTime() {
        return time;
    }

    public String getMode_s() {
        return mode_s;
    }

    public String getCallsign() {
        return callsign;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getPosition() {
        return position;
    }

    public Long getAltitude_feet() {
        return altitude_feet;
    }

    public Long getSquawk() {
        return squawk;
    }

    public Long getGround_speed() {
        return ground_speed;
    }

    public Long getIgnore() {
        return ignore;
    }

    public Long getVspeed() {
        return vspeed;
    }

    public String getOn_ground() {
        return on_ground;
    }

    public Long getRadar_id() {
        return radar_id;
    }


    @Override
    public String toString() {
        return "FlightData{" +
                "time=" + time +
                ", mode_s='" + mode_s + '\'' +
                ", callsign='" + callsign + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", position='" + position + '\'' +
                ", altitude_feet=" + altitude_feet +
                ", squawk=" + squawk +
                ", ground_speed=" + ground_speed +
                ", ignore=" + ignore +
                ", vspeed=" + vspeed +
                ", on_ground='" + on_ground + '\'' +
                ", radar_id=" + radar_id +
                '}';
    }
}
