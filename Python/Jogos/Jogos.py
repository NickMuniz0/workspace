import Forca 
import Adivinhacao

print("********************************")
print("*********Escolha o Jogo!********")
print("********************************")

print("(1) - Forca  (2) - Adivinhacao")
jogo= int(input("Qual o jogo? "))


if(jogo==1):
    print("Jogando Forca")
    Forca.jogar()
elif(jogo==2):
    print("Jogando Adivinhacao")
    Adivinhacao.jogar()
else:
    print("Para de palhacada e escolha entre as opcoes demente!")