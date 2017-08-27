/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.es.dsic.quep.utils;

import java.math.BigDecimal;

/**
 *
 * @author agna8685
 */
public class ResponseEstimate {

    private BigDecimal sumRequiered;
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal avgResilience;    

    public ResponseEstimate() {
    }

    public BigDecimal getSumRequiered() {
        return sumRequiered;
    }

    public void setSumRequiered(BigDecimal sumRequiered) {
        this.sumRequiered = sumRequiered;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getAvgResilience() {
        return avgResilience;
    }

    public void setAvgResilience(BigDecimal avgResilience) {
        this.avgResilience = avgResilience;
    }

}
