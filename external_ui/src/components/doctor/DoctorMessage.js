import {useEffect, useImperativeHandle, useRef, useState} from "react";
import styles from '../../layouts/body/style.module.css'
import {SendApiService} from "../../service/SendApiService";
import {Chat, EMPLOYYEE, MinioUrl, PATIENT, WEBSOCKET} from "../../ApiConstant";
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
        SendApiService.getRequest(Chat.Message.getMessageByReceiverId(relationShip.patientId, pageAble), {}, response => {
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
            SendApiService.postRequest(Chat.Message.sendImage(relationShip.patientId), formData, {
                'Content-Type': 'multipart/form-data'
            }, response => {

            }, error => {

            })
        } else {
            const newMessage = {
                content: content,
                receiverId: relationShip.patientId
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

    const handleClickUpload = () => {
        if (fileInputRef.current) {
            fileInputRef.current.click(); // Kích hoạt input file khi nhấp vào SVG
        }
    }

    return (<div className="flex-1 bg-white p-4">
        <h2>{(() => {
            if (!relationShip.patient) {
                return null
            }

            const fullName = relationShip.patient.fullName
            return `Bệnh nhân  ${fullName.firstName} ${fullName.middleName} ${fullName.lastName}`
        })()}</h2>

        {relationShip.id && pageAble.page !== pageAble.total && <button onClick={() => setPableAble((prev => {
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

            return <div key={key}
                        className={`flex gap-2.5 items-start ${styles.marginTop10} ${styles.marginLeft5} ${message.senderId === JwtService.geUserFromToken() ? 'ml-auto flex-row-reverse' : ''}`}>
                <div
                    className={`flex flex-col w-full max-w-[320px] leading-1.5 p-4 border-gray-200 rounded-e-xl rounded-es-xl ${
                        message.senderId === JwtService.geUserFromToken() ? 'bg-blue-500 dark:bg-blue-700' : 'bg-gray-100 dark:bg-gray-700'
                    }`}>
                    <p className="text-sm font-normal py-2.5 text-gray-900 dark:text-white">{message.content}</p>
                    <div className="flex items-center space-x-2 rtl:space-x-reverse">
                        <span
                            className="text-sm font-normal text-black-500 dark:text-gray-400">{FormatCreatedDate(message.createdAt)}</span>
                    </div>
                </div>
            </div>
        })}

        {previewImage &&
            <div>
                <img className={styles.previewChatImage} src={previewImage}/>
                <button onClick={() => deleteSelectedImage()}>Xoá ảnh này</button>
            </div>
        }

        {relationShip.id && <form>
            <div
                className="w-full mb-4 border border-gray-200 rounded-lg bg-gray-50 dark:bg-gray-700 dark:border-gray-600">
                <div className="px-4 py-2 bg-white rounded-t-lg dark:bg-gray-800">
                    <label htmlFor="comment" className="sr-only">Your comment</label>
                    <textarea id="comment" rows="4"
                              className="w-full px-0 text-sm text-gray-900 bg-white border-0 dark:bg-gray-800 focus:ring-0 dark:text-white dark:placeholder-gray-400"
                              placeholder="Nhập bình luận"
                              value={content} onChange={(e) => setContent(e.target.value)}></textarea>
                </div>
                <div className="flex items-center justify-between px-3 py-2 border-t dark:border-gray-600">
                    <button type="button"
                            className="inline-flex items-center py-2.5 px-4 text-xs font-medium text-center text-white bg-blue-700 rounded-lg focus:ring-4 focus:ring-blue-200 dark:focus:ring-blue-900 hover:bg-blue-800"
                            onClick={() => onclickSendButton()}>Gửi</button>
                    <div className="flex ps-0 space-x-1 rtl:space-x-reverse sm:ps-2">
                        <button type="button"
                                className="inline-flex justify-center items-center p-2 text-gray-500 rounded cursor-pointer hover:text-gray-900 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-white dark:hover:bg-gray-600">
                            <div>
                                <svg className="w-4 h-4" aria-hidden="true" xmlns="http://www.w3.org/2000/svg"
                                     fill="none"
                                     viewBox="0 0 12 20"
                                     onClick={() => handleClickUpload()}>
                                    <path stroke="currentColor" stroke-linejoin="round" stroke-width="2"
                                          d="M1 6v8a5 5 0 1 0 10 0V4.5a3.5 3.5 0 1 0-7 0V13a2 2 0 0 0 4 0V6"/>
                                </svg>

                                <input
                                    type="file"
                                    ref={fileInputRef}
                                    accept="image/*"
                                    onChange={handleImageChange}
                                    className="absolute top-0 left-0 opacity-0 cursor-pointer"/>
                            </div>
                        </button>
                    </div>
                </div>
            </div>
        </form>}

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

const DoctorMessage = () => {
    const [relationShipMap, setRelationShipMap] = useState(new Map())
    const [selectedRelationShip, setSelectedRelationShip] = useState('')
    const [page, setPage] = useState(0)
    const [webSocket, setWebSocket] = useState(new WebSocketService())
    const receivedMesageRef = useRef()

    const getPatientInformation = (patientIdsMap) => {
        const ids = []
        for (const [key, value] of patientIdsMap) {
            ids.push(key)
        }
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(PATIENT.PATIENT_API.getByIds(), ids, {}, response => {
                for (const patient of response.data) {
                    const relationShip = patientIdsMap.get(patient.id)
                    relationShip.patient = patient
                    resolve()
                }
            }, error => {
                reject(error)
            })
        })
    }

    const findRelationShipOfDoctor = () => {
        SendApiService.getRequest(Chat.RelationShip.findRelationShipOfDoctor(page), {}, async response => {
            const tempMap = new Map()
            const patientIdsMap = new Map()
            const content = response.data.content
            if (!content) {
                return
            }
            for (const relationShip of content) {
                tempMap.set(relationShip.id, relationShip)
                patientIdsMap.set(relationShip.patientId, relationShip)
            }
            await getPatientInformation(patientIdsMap)
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
        findRelationShipOfDoctor()
        connectToWebSocket()

        return () => {
            disconnect()
        }
    }, []);

    return (
        <div className="flex">
            {/* Danh sách liên hệ */}
            <div className="w-1/4 bg-gray-100 border-r border-gray-300 p-4 h-full sticky top-0 overflow-y-auto h-screen">
                <h2 className="text-lg font-bold mb-4">Danh sách liên hệ</h2>
                <ul>
                    {[...relationShipMap].map(([key, value]) => {
                        const patient = value.patient;
                        if (!patient) {
                            return null;
                        }
                        const fullName = patient.fullName;
                        return (
                            <li
                                className={`mb-2 p-3 border border-gray-300 rounded-lg shadow-sm hover:bg-gray-100 transition ${styles.cursorPointer}`}
                                key={key}
                                onClick={() => setSelectedRelationShip(value)}>
                                {fullName.firstName + " " + fullName.middleName + " " + fullName.lastName}
                            </li>
                        );
                    })}
                </ul>
            </div>

            {/* Khung chat */}
            <div className="w-3/4 flex-grow h-full overflow-y-auto">
                <ChatWindow
                    relationShip={selectedRelationShip}
                    webSocket={webSocket}
                    callBack={receivedMesageRef}
                />
            </div>
        </div>
    )
}

export {DoctorMessage}