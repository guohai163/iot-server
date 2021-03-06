# coding: utf-8
from gevent import socket
from locust import TaskSet, task, between, User, events
import time
import uuid


class SocketClient(object):

    def __init__(self):
        # 仅在新建实例的时候创建socket.
        self._socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def __getattr__(self, name):
        conn = self._socket

        def wrapper(*args, **kwargs):
            # 根据后面做的业务类，不同的方法做不同的处理
            if name == "connect":
                try:
                    conn.connect(args[0])
                except Exception as e:
                    print(e)
            elif name == "send":
                print(args[0])
                conn.sendall(args[0].encode())
                data = conn.recv(1024)
                print(data)
            elif name == "close":
                conn.close()

        return wrapper


class UserBehavior(TaskSet):
    def on_start(self):
        # 该方法每用户启动时调用进行连接打开
        self.client.connect((self.user.host, self.user.port))
        self.send_login()

    def on_stop(self):
        # 该方法当程序结束时每用户进行调用，关闭连接
        self.client.close()

    def send_login(self):
        start_time = time.time()

        tx_no = int(round(start_time * 1000))
        dataBody = '{"msgType": 10, "devId": "%s", "version":"1.0", "txNo": "%d", "sign": "xxxxx"}' % (
            uuid.uuid1(), tx_no)

        # 接下来做实际的网络调用，并通过request_failure和request_success方法分别统计成功和失败的次数以及所消耗的时间
        try:
            self.client.send(dataBody)
        except Exception as e:
            total_time = int((time.time() - start_time) * 1000)
            events.request_failure.fire(request_type="iot_server", name="login", response_time=total_time,
                                        response_length=0, exception=e)
        else:
            total_time = int((time.time() - start_time) * 1000)
            events.request_success.fire(request_type="iot_server", name="login", response_time=total_time,
                                        response_length=0)

    @task(1)
    def send_20(self):
        start_time = time.time()

        tx_no = int(round(start_time * 1000))
        dataBody = ' {"msgType": 20, "txNo": "%d"}' % tx_no

        try:
            self.client.send(dataBody)
        except Exception as e:
            total_time = int((time.time() - start_time) * 1000)
            events.request_failure.fire(request_type="iot_server", name="msg_20", response_time=total_time,
                                        response_length=0, exception=e)
        else:
            total_time = int((time.time() - start_time) * 1000)
            events.request_success.fire(request_type="iot_server", name="msg_20", response_time=total_time,
                                        response_length=0)


class SocketLocust(User):
    abstract = True

    def __init__(self, *args, **kwargs):
        super(SocketLocust, self).__init__(*args, **kwargs)
        self.client = SocketClient()


class SocketUser(SocketLocust):
    # 目标地址
    host = "127.0.0.1"
    # 目标端口
    port = 4100
    tasks = [UserBehavior]
    wait_time = between(50, 70)
