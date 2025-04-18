from datetime import datetime,timedelta
from pytz import timezone


def get_data():
    data_e_hora_atuais = datetime.now()
    fuso_horario = timezone("America/Sao_Paulo")
    return data_e_hora_atuais.astimezone(fuso_horario).strftime("%Y-%m-%d %H:%M:%S.%f")


print(get_data())