package com.krishnchinya.personalhealthmonitoringsystem.other;

/**
 * Created by shwethaparmar on 4/19/17.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.http.Field;

public class FoodItems {
    @SerializedName("total_hits")
    public String totalHits;

    @SerializedName("max_score")
    public String max_score_info;

    @SerializedName("hits")
    public List<hit> hits;


    public String getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(String totalHits) {
        this.totalHits = totalHits;
    }


    public String getMax_score_info() {
        return max_score_info;
    }

    public void setMax_score_info(String max_score_info) {
        this.max_score_info = max_score_info;
    }

    public List<hit> getHits() {
        return hits;
    }

    public void setHits(List<hit> hits) {
        this.hits = hits;
    }

    public static final class hit{
        @SerializedName("_index")
        public String _index;
        @SerializedName("_type")
        public String _type;
        @SerializedName("_id")
        public String _id;
        @SerializedName("_score")
        public String _score;
        @SerializedName("fields")
        public fields fields;

        public String get_index() {
            return _index;
        }

        public void set_index(String _index) {
            this._index = _index;
        }



        public String get_type() {
            return _type;
        }

        public void set_type(String _type) {
            this._type = _type;
        }



        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }



        public String get_score() {
            return _score;
        }

        public void set_score(String _score) {
            this._score = _score;
        }


        public hit.fields getFields() {
            return fields;
        }

        public void setFields(hit.fields fields) {
            this.fields = fields;
        }

        public static final class fields{
            @SerializedName("item_id")
            public String item_id;
            @SerializedName("item_name")
            public String item_name;
            @SerializedName("brand_name")
            public String brand_name;
            @SerializedName("nf_calories")
            public String nf_calories;
            @SerializedName("nf_total_fat")
            public String nf_total_fat;
            @SerializedName("nf_serving_size_qty")
            public String nf_serving_size_qty;
            @SerializedName("nf_serving_size_unit")
            public String nf_serving_size_unit;


            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }


            public String getItem_name() {
                return item_name;
            }

            public void setItem_name(String item_name) {
                this.item_name = item_name;
            }



            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public String getNf_calories() {
                return nf_calories;
            }

            public void setNf_calories(String nf_calories) {
                this.nf_calories = nf_calories;
            }



            public String getNf_total_fat() {
                return nf_total_fat;
            }

            public void setNf_total_fat(String nf_total_fat) {
                this.nf_total_fat = nf_total_fat;
            }

            public String getNf_serving_size_qty() {
                return nf_serving_size_qty;
            }

            public void setNf_serving_size_qty(String nf_serving_size_qty) {
                this.nf_serving_size_qty = nf_serving_size_qty;
            }



            public String getNf_serving_size_unit() {
                return nf_serving_size_unit;
            }

            public void setNf_serving_size_unit(String nf_serving_size_unit) {
                this.nf_serving_size_unit = nf_serving_size_unit;
            }




        }

    }


}
