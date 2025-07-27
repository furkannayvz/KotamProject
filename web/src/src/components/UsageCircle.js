import React from "react";
import {
  CircularProgressbar,
  buildStyles
} from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";

/**
 * UsageCircle
 * @param {string} label - Gösterilecek başlık (ör: Internet, Minutes, SMS)
 * @param {number} value - Kullanılan miktar
 * @param {number} max - Toplam miktar
 * @param {string} unit - Birim (ör: GB, min, SMS)
 * @param {string} color - Ana renk (default: "#0e3970")
 * @param {string} sublabel - Alt başlık (ör: "Remaining", "Kalan")
 * @param {string} expireText - "Expire" bilgisi (ör: "Resets in 10 days")
 */
function UsageCircle({
  label,
  value,
  max,
  unit,
  color = "#0e3970",
  sublabel,
  expireText
}) {
  // Kalan quota (yüzde)
  const percentage = max > 0 ? Math.round((value / max) * 100) : 0;

  return (
    <div className="usage-circle">
      <div style={{ width: 84, height: 84 }}>
        <CircularProgressbar
          value={percentage}
          text={`${value} ${unit}`}
          maxValue={100}
          styles={buildStyles({
            pathColor: color,
            textColor: "#0e3970",
            trailColor: "#e5eaf4",
            textSize: "17px",
            strokeLinecap: "round"
          })}
        />
      </div>
      <div className="usage-desc">
        <div className="usage-label">{label}</div>
        {sublabel && <div className="usage-sublabel">{sublabel}</div>}
        <div className="usage-value">{value} / {max} {unit}</div>
        {expireText && <div className="usage-expire">{expireText}</div>}
      </div>
    </div>
  );
}

export default UsageCircle;