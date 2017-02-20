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
public class Result {
     private BigDecimal complete;
        private BigDecimal perComplete;

        public Result() {
        }

        public BigDecimal getComplete() {
            return complete;
        }

        public void setComplete(BigDecimal complete) {
            this.complete = complete;
        }

        public BigDecimal getPerComplete() {
            return perComplete;
        }

        public void setPerComplete(BigDecimal perComplete) {
            this.perComplete = perComplete;
        }
}
