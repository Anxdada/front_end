import React from 'react';
import { Card, Button, Modal } from 'antd';
import './ui.less';

export default class Modals extends React.Component {

    state = {
        showModal1: false,
        showModal2: false,
        showModal3: false,
        showModal4: false,
    }

    handleOpen = (type) => {
        this.setState({
            [type]: true,
        })
    }

    handleOpenConfirm = (type) => {
        Modal[type]({
            title: '确认?',
            content: '你确定学会了React了吗?',
            onOk(){
                console.log("Ok");
            },
            onCancel(){
                console.log("Cancel");
            }
        })
    }

    render() {
        return(
            <div>
                <Card title="基础模态框" className="Card">
                    <Button type="primary" onClick={() => this.handleOpen('showModal1')}>Open</Button>
                    <Button type="primary" onClick={() => this.handleOpen('showModal2')}>自定义页脚</Button>
                    <Button type="primary" onClick={() => this.handleOpen('showModal3')}>顶部20px弹框</Button>
                    <Button type="primary" onClick={() => this.handleOpen('showModal4')}>水平垂直居中</Button>
                </Card>
                <Modal
                    title="React"
                    visible={this.state.showModal1}
                    onCancel={()=>{
                        this.setState({
                            showModal1: false,
                        })
                    }}
                >
                    <p> 这是一个Modal1 的演示 </p>
                </Modal>
                <Modal
                    title="React"
                    visible={this.state.showModal2}
                    okTex="确定"
                    cancelText="算了, 大哥"
                    onCancel={()=>{
                        this.setState({
                            showModal2: false,
                        })
                    }}
                >
                    <p> 这是一个Modal2 的演示 </p>
                </Modal>
                <Modal
                    title="React"
                    visible={this.state.showModal3}
                    style={{top: 20,}}
                    onCancel={()=>{
                        this.setState({
                            showModal3: false,
                        })
                    }}
                >
                    <p> 这是一个Modal3 的演示 </p>
                </Modal>
                <Modal
                    title="React"
                    wrapClassName="vertical-center-modal"
                    visible={this.state.showModal4}
                    onCancel={()=>{
                        this.setState({
                            showModal4: false,
                        })
                    }}
                >
                    <p> 这是一个Modal4 的演示 </p>
                </Modal>
                <Card title="信息确认框" className="Card">
                    <Button type="primary" onClick={() => this.handleOpenConfirm('confirm')}>Confirm</Button>
                    <Button type="primary" onClick={() => this.handleOpenConfirm('info')}>Info</Button>
                    <Button type="primary" onClick={() => this.handleOpenConfirm('success')}>Success</Button>
                    <Button type="primary" onClick={() => this.handleOpenConfirm('warning')}>Warning</Button>
                </Card>
            </div>
        );
    }
}