package cn.com.wh.ring.network.request;

import com.amap.api.location.AMapLocation;

/**
 * Created by Hui on 2017/11/30.
 */

public class Address {
    private String country;
    private String province;
    private String city;
    private String district;
    private Double lng;
    private Double lat;

    public Address() {
    }

    public Address(AMapLocation aMapLocation) {
        this.country = aMapLocation.getCountry();
        this.province = aMapLocation.getProvince();
        this.city = aMapLocation.getCity();
        this.district = aMapLocation.getDistrict();
        this.lng = aMapLocation.getLongitude();
        this.lat = aMapLocation.getLatitude();
    }

    public Address(cn.com.wh.ring.database.bean.Address address) {
        this.country = address.getCountry();
        this.province = address.getProvince();
        this.city = address.getCity();
        this.district = address.getDistrict();
        this.lng = address.getLng();
        this.lat = address.getLat();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
