import {useEffect, useImperativeHandle, useRef, useState} from "react";
import styles from '../../layouts/body/style.module.css'
import {SendApiService} from "../../service/SendApiService";
import {Chat, EMPLOYYEE, MinioUrl, WEBSOCKET} from "../../ApiConstant";
import WebSocketService from "../../service/WebSocketService";
import {JwtService} from "../../service/JwtService";
import {FormatCreatedDate} from "../../service/TimeService";

const ChatWindow = ({relationShip, callBack}) => {
    const [thisRelationShip, setThisRelationShip] = useState({})
    const [pageAble, setPableAble] = useState({page: 0})
    const [messages, setMessages] = useState(new Map)
    const [content, setContent] = useState('')
    const [previewImage, setPreviewImage] = useState(null)
    const fileInputRef = useRef(null)
    const [selectedImage, setSelectedImage] = useState(null)
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedWatchImage, setSelectedWatchImage] = useState("");

    const openModal = (imageUrl) => {
        setSelectedWatchImage(imageUrl);
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedWatchImage("");
    };

    const getAllMessage = () => {
        SendApiService.getRequest(Chat.Message.getMessageByReceiverId(relationShip.doctorId, pageAble), {}, response => {
            const tempMap = new Map(messages)
            for (const message of response.data.content) {
                tempMap.set(message.id, message)
            }
            setMessages(tempMap)

            const newPageAble = {
                page: pageAble.page,
                total: response.data.totalPages
            }
            setPableAble(newPageAble)
        }, error => {

        })
    }

    useEffect(() => {
        getAllMessage()
        setThisRelationShip(relationShip)
    }, [relationShip.id, pageAble.page]);

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file && file.type.startsWith('image/')) {
            const preview = URL.createObjectURL(file)
            setPreviewImage(preview)
            setSelectedImage(file)
        }
    };

    const onclickSendButton = () => {
        if (!content && !selectedImage) {
            return
        }

        if (selectedImage) {
            const formData = new FormData()
            formData.append("image", selectedImage)
            SendApiService.postRequest(Chat.Message.sendImage(relationShip.doctorId), formData, {
                'Content-Type': 'multipart/form-data'
            }, response => {

            }, error => {

            })
        } else {
            const newMessage = {
                content: content,
                receiverId: relationShip.doctorId
            }

            SendApiService.postRequest(Chat.Message.getUrl(), newMessage, {}, response => {

            }, error => {

            })
        }

        setSelectedImage(null)
        setContent('')
        setPreviewImage('')
        fileInputRef.current.value = ''
    }

    const deleteSelectedImage = () => {
        setPreviewImage(prev => {
            URL.revokeObjectURL(prev)
            return null
        })
        fileInputRef.current.value = ''
    }

    const showMessage = (data) => {
        console.log(data, relationShip)
        const newMessage = JSON.parse(data).data
        if (newMessage.relationShipId === relationShip.id) {
            let tempMap = null
            if(messages.has(newMessage.id)){
                tempMap = new Map(messages)
                tempMap.set(newMessage.id, newMessage)
            }else{
                tempMap = new Map([[newMessage.id, newMessage], ...messages])
            }
            setMessages(tempMap)
        }
    };

    useImperativeHandle(callBack, () => ({
        receivedMessage(data) {
            showMessage(data)
        }
    }));

    return (<div>
        <h2>{(() => {
            if (!relationShip.doctor) {
                return null
            }

            const fullName = relationShip.doctor.fullName
            return `Bác sĩ  ${fullName.firstName} ${fullName.middleName} ${fullName.lastName}`
        })()}</h2>

        {pageAble.page !== pageAble.total && <button onClick={() => setPableAble((prev => {
            const newPage = {
                page: prev.page + 1,
                total: 0
            }
            return newPage
        }))}>Tải tin nhắn cũ hơn</button>}

        {[...messages].reverse().map(([key, message]) => {
            if (message.imageUrl) {
                return <div key={key}
                            className={`border border-gray-300 rounded-lg p-4 w-fit ${styles.marginTop10} ${styles.marginLeft5} ${message.senderId === JwtService.geUserFromToken() ? 'ml-auto flex-row-reverse' : ''}`}>
                        <div className="flex gap-4 items-center">
                            <div className="flex flex-col items-center">
                                <label className="text-sm font-medium mb-2">Ảnh ban đầu</label>
                                <img
                                    onClick={() => openModal(MinioUrl.downloadFile(message.imageUrl))}
                                    className="w-48 h-48 object-cover rounded-lg cursor-pointer"
                                    src={MinioUrl.downloadFile(message.imageUrl)}
                                    alt="Ảnh ban đầu"
                                />
                            </div>
                            <div className="flex flex-col items-center">
                                <label className="text-sm font-medium mb-2">Ảnh được chuẩn đoán</label>
                                <img
                                    onClick={() => openModal(MinioUrl.downloadFile(message.detectedImageUrl))}
                                    className="w-48 h-48 object-cover rounded-lg cursor-pointer"
                                    src={MinioUrl.downloadFile(message.detectedImageUrl)}
                                    alt="Ảnh được chuẩn đoán"
                                />
                            </div>
                        </div>

                        <div className="flex items-center space-x-2 rtl:space-x-reverse">
                            <span
                                className="text-sm font-normal text-black-500 dark:text-gray-400">{FormatCreatedDate(message.createdAt)}</span>
                        </div>
                </div>
            }

            return <div key={key} className={`flex gap-2.5 items-start ${styles.marginTop10} ${styles.marginLeft5} ${message.senderId === JwtService.geUserFromToken() ? 'ml-auto flex-row-reverse' : ''}`}>
                <div
                    className={`flex flex-col w-full max-w-[320px] leading-1.5 p-4 border-gray-200 rounded-e-xl rounded-es-xl ${
                        message.senderId === JwtService.geUserFromToken() ? 'bg-blue-500 dark:bg-blue-700' : 'bg-gray-100 dark:bg-gray-700'
                    }`}>
                    <p className="text-sm font-normal py-2.5 text-gray-900 dark:text-white">{message.content}</p>
                    <div className="flex items-center space-x-2 rtl:space-x-reverse">
                        <span className="text-sm font-normal text-black-500 dark:text-gray-400">{FormatCreatedDate(message.createdAt)}</span>
                    </div>
                </div>
            </div>
        })}

        <textarea value={content} onChange={(e) => setContent(e.target.value)}/>
        <button onClick={() => onclickSendButton()}>Gửi</button>

        {previewImage &&
            <div>
                <img className={styles.previewChatImage} src={previewImage}/>
                <button onClick={() => deleteSelectedImage()}>Xoá ảnh này</button>
            </div>
        }
        <div>
            <label>Chọn ảnh</label>
            <input
                type="file"
                ref={fileInputRef}
                accept="image/*"
                onChange={handleImageChange}/>
        </div>

        {isModalOpen && (
            <div
                className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-75"
                onClick={closeModal}>
                    <div className="relative">
                        <img
                            className="max-w-full max-h-screen"
                            src={selectedWatchImage}
                            alt="Phóng to"/>
                        <button
                            className="absolute top-2 right-2 text-white text-2xl"
                            onClick={closeModal}>
                            ×
                        </button>
                    </div>
                </div>
            )}
    </div>)
}

const Message = () => {
    const [relationShipMap, setRelationShipMap] = useState(new Map())
    const [selectedRelationShip, setSelectedRelationShip] = useState('')
    const [page, setPage] = useState(0)
    const [webSocket, setWebSocket] = useState(new WebSocketService())
    const receivedMesageRef = useRef()

    const getDoctorInformation = (doctorIdsMap) => {
        const ids = []
        for (const [key, value] of doctorIdsMap) {
            ids.push(key)
        }
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(EMPLOYYEE.findByIds(), ids, {}, response => {
                for (const doctor of response.data) {
                    const relationShip = doctorIdsMap.get(doctor.id)
                    relationShip.doctor = doctor
                    resolve()
                }
            }, error => {
                reject(error)
            })
        })
    }

    const findRelationShipOfPatient = () => {
        SendApiService.getRequest(Chat.RelationShip.findRelationShipsOfPatient(page), {}, async response => {
            const tempMap = new Map()
            const doctorIdsMap = new Map()
            const content = response.data.content
            if (!content) {
                return
            }
            for (const relationShip of content) {
                tempMap.set(relationShip.id, relationShip)
                doctorIdsMap.set(relationShip.doctorId, relationShip)
            }
            await getDoctorInformation(doctorIdsMap)
            setRelationShipMap(tempMap)

        }, error => {

        })
    }

    const connectToWebSocket = () => {
        const topics = [WEBSOCKET.chat(JwtService.geUserFromToken())]
        webSocket.connectAndSubscribe(topics, (message) => {
            if (receivedMesageRef.current) {
                receivedMesageRef.current.receivedMessage(message);
            }
        })
    }

    const disconnect = () => {
        webSocket.disconnect()
    }

    useEffect(() => {

    }, []);

    useEffect(() => {
        findRelationShipOfPatient()
        connectToWebSocket()

        return () => {
            disconnect()
        }
    }, []);

    return (<>
        <h2>Tin nhắn tư vấn</h2>
        <div className={`${styles.height100} ${styles.divFlex}`}>
            <div className={`${styles.width20} ${styles.height100} ${styles.scrollY}`}>
                {[...relationShipMap].map(([key, value]) => {
                    const doctor = value.doctor
                    if (!doctor) {
                        return null
                    }
                    const fullName = doctor.fullName
                    return <div className={styles.cursorPointer} key={key}
                                onClick={() => setSelectedRelationShip(value)}>
                        {fullName.firstName + " " + fullName.middleName + " " + fullName.lastName}
                    </div>
                })}
            </div>
            <div className={`${styles.width80} ${styles.scrollY}`}>
                <ChatWindow relationShip={selectedRelationShip} webSocket={webSocket} callBack={receivedMesageRef}/>
            </div>
        </div>
    </>)
}

export {Message}