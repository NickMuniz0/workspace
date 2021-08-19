import random

def jogar():
    print("********************************")
    print("Bem vindo no jogo de Advinhacao!")
    print("********************************")

                    # round(random.random() * 100)
                    #int(random.random() * 100)
    numero_random = random.randrange(1,101)
    print(numero_random)

    numero_secreto = numero_random
    total_de_tentativas = 0
    pontos= 1000

    print("Qual nivel de dificuldade?")
    print("(1) Fácil (2)Médio (3) Difícil")
    nivel = int(input("Defina o nivel: "))


    if(nivel ==1):
        total_de_tentativas=20
    elif(nivel ==2):
        total_de_tentativas=10
    else:
        total_de_tentativas=5


    rodada=1
    while(rodada<=total_de_tentativas):
    # for rodada in range(1,total_de_tentativas +1):
        print("Tentativa: {} de {}".format(rodada,total_de_tentativas))
        numero_escolhido = input("Digite seu numero entre {} e {}: ".format(1,100))
        numero_escolhido_convertido  = int(numero_escolhido)
        acertou = numero_escolhido_convertido== numero_secreto
        menor = numero_escolhido_convertido< numero_secreto
        maior = numero_escolhido_convertido> numero_secreto

        if(numero_escolhido_convertido < 1 or numero_escolhido_convertido > 100 ):
            print("deve digitar entre 1 e 100")   
            rodada-1
            continue

        if(acertou):
            print("Acertou! {} pontos".format(pontos))
            break
        else:
            if(maior):
                print("Errou!Seu chute Foi maior.")
            elif(menor):
                print("Errou!Seu chuto Foi menor")
                pontos_perdidos = abs(numero_secreto - numero_escolhido_convertido)
                pontos = pontos - pontos_perdidos

        rodada+=1


    print("FIM DO JOGO")
    # for rodada in range(1,10,2):  for rodada in [1,3,4,5]
    #     print(rodada)


if(__name__== "__main__"):
    jogar()

