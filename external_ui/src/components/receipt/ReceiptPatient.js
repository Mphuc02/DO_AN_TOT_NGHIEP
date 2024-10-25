import {useState} from "react";
import styles from '../../layouts/body/style.module.css'
import {ReceiptWithAppointment} from "./ReceiptWithAppointment";

function ReceiptPatient(){
    const [selectedTab, setSelectedTab] = useState(1)

    return (
        <div>
            <h2>Tiếp đón bệnh nhân</h2>
            <div className={styles.divFlex}>
                <div onClick={() => setSelectedTab(1)}>Bệnh nhân lần đầu khám</div>
                <div onClick={() => setSelectedTab(2)}>Bệnh nhân không có lịch hẹn</div>
                <div onClick={() => setSelectedTab(3)}>Bệnh nhân có lịch hẹn</div>
            </div>

            {selectedTab === 3 && <ReceiptWithAppointment />}
        </div>
    )
}

export default ReceiptPatient