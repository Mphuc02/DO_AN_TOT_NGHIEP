import React, {forwardRef, useImperativeHandle, useState} from 'react';
import styles from '../../layouts/body/style.module.css';

const StatusBar = forwardRef(({ numberOfCircles, labels, callBack }) => {
    const [circleStatus, setCircleStatus] = useState(Array(numberOfCircles).fill(null));
    const [messages, setMessages] = useState(Array(numberOfCircles).fill(null))

    const getCircleColor = (status) => {
        if (status === 'OK') {
            return 'green';
        } else if (status === 'ERROR') {
            return 'red';
        }
        return 'gray';
    };

    useImperativeHandle(callBack, () => ({
        triggerChildFunction(data) {
            console.log(data)
            const { index, status, message } = data;
            setCircleStatus(prevStatus => {
                const updatedStatus = [...prevStatus];
                updatedStatus[index] = status;
                return updatedStatus
            });
            setMessages(prevMessages => {
                const updatedMessage = [...prevMessages]
                updatedMessage[index] = message
                return updatedMessage
            })
            console.log(messages)
        }
    }));

    return (
        <div>
            <div className={styles.status_container}>
                {circleStatus.map((status, index) => (
                    <React.Fragment key={index}>
                        <div>
                            <span className={styles.circleLabel}>{labels[index]}</span>
                            <div
                                className={styles.circle}
                                style={{backgroundColor: getCircleColor(status)}}>
                            </div>
                            <span className={styles.circleLabel}>{messages[index]}</span>
                        </div>

                        {index < circleStatus.length - 1 && (
                            <div className={styles.connector}></div>
                        )}
                    </React.Fragment>
                ))}
            </div>
        </div>
    );
})

export default StatusBar;