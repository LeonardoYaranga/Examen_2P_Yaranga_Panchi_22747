from datetime import datetime, timedelta
from locust import HttpUser, task, between
import random

class MyUser(HttpUser):
    host = "http://localhost:8000"

    wait_time = between(1, 3)

    @task
    def crear_eventos(self):
        value =  round(random.uniform(0, 60), 2)
        start_date = datetime(2025, 7, 7, 0, 0, 0) - timedelta(days=30)
        end_date = datetime(2025, 7, 7, 23, 59, 59)
        random_seconds = random.randint(0, int((end_date - start_date).total_seconds()))
        random_timestamp = start_date + timedelta(seconds=random_seconds)
        timestamp_str = random_timestamp.strftime("%Y-%m-%dT%H:%M:%SZ")
        payload ={
                "sensorId": f"sensor-00{random.randint(1, 3)}",
                "type": random.choice(["temperature", "humidity", "seismic"]),
                "value": value,
                "timestamp": timestamp_str
                }
        self.client.post("/api/conjunta/2p/sensor-readings", json=payload)
