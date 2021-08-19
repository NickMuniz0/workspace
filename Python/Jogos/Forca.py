import random 

def jogar():

    imprime_mensagem_de_abertura()
    palavra_secreta= carrega_palavra_secreta()
    letra_acertadas = incializa_letras_acertadas(palavra_secreta)
    enforcou = False
    acertou = False
    erros=0    
    imprimeVisualizacaoDaPalavraOculta(letra_acertadas)
    while(not enforcou and not acertou):    
        erros=verifica_palavras_corretas(palavra_secreta,letra_acertadas,erros)
        enforcou =erros ==7
        acertou = "_" not in letra_acertadas
    imprimeMensagemFinal(acertou,palavra_secreta)

def verifica_palavras_corretas(palavra_secreta,letra_acertadas,erros):
        chute = pede_chute()
        if(chute in palavra_secreta):
            marca_chute_correto(palavra_secreta,chute,letra_acertadas)
        else:
            erros+=1
        desenha_forca(erros)
        imprimePalavraOcultaDescobertas(letra_acertadas)
        return erros
        
def imprimePalavraOcultaDescobertas(letra_acertadas):
    print(letra_acertadas)

def imprimeVisualizacaoDaPalavraOculta(letra_acertadas):
    print(letra_acertadas)

def imprimeMensagemFinal(acertou,palavra_secreta):
    if(acertou): 
        imprimeVencedor()
    else:
        imprimePerdedor(palavra_secreta)

def imprimeVencedor():
    print("Parabéns, você ganhou!")
    print("       ___________      ")
    print("      '._==_==_=_.'     ")
    print("      .-\\:      /-.    ")
    print("     | (|:.     |) |    ")
    print("      '-|:.     |-'     ")
    print("        \\::.    /      ")
    print("         '::. .'        ")
    print("           ) (          ")
    print("         _.' '._        ")
    print("        '-------'       ")

def imprimePerdedor(palavra_secreta):
    print("Puxa, você foi enforcado!")
    print("A palavra era {}".format(palavra_secreta))
    print("    _______________         ")
    print("   /               \       ")
    print("  /                 \      ")
    print("//                   \/\  ")
    print("\|   XXXX     XXXX   | /   ")
    print(" |   XXXX     XXXX   |/     ")
    print(" |   XXX       XXX   |      ")
    print(" |                   |      ")
    print(" \__      XXX      __/     ")
    print("   |\     XXX     /|       ")
    print("   | |           | |        ")
    print("   | I I I I I I I |        ")
    print("   |  I I I I I I  |        ")
    print("   \_             _/       ")
    print("     \_         _/         ")
    print("       \_______/           ")

def marca_chute_correto(palavra_secreta,chute,letra_acertadas):
    index  = 0
    for letra in palavra_secreta:
        if(chute == letra):
            # print("Encontrei a letra {}  na posicao {} ".format(letra,index))
            letra_acertadas[index]=letra
        index+=1
    return letra_acertadas

def pede_chute():
    return input("Qual letra?").strip().upper()

def imprime_mensagem_de_abertura():

    print("********************************")
    print("Bem vindo no jogo de Advinhacao!")
    print("********************************")

def carrega_palavra_secreta():
    arquivo= open('palavras.txt','r')
    palavras=[]
    for linha in arquivo:
        palavras.append(linha.strip())
    print(palavras)
    arquivo.close()

    posicao_da_palavra = random.randrange(0,len(palavras))
    
    return palavras[posicao_da_palavra].upper()

def incializa_letras_acertadas(palavra_secreta):
    # for letra in palavra_secreta:
    #     letra_acertadas.append('_')

    return ['_' for letra in palavra_secreta]

def desenha_forca(erros):
    print("  _______     ")
    print(" |/      |    ")

    if(erros == 1):
        print(" |      (_)   ")
        print(" |            ")
        print(" |            ")
        print(" |            ")

    if(erros == 2):
        print(" |      (_)   ")
        print(" |      \     ")
        print(" |            ")
        print(" |            ")

    if(erros == 3):
        print(" |      (_)   ")
        print(" |      \|    ")
        print(" |            ")
        print(" |            ")

    if(erros == 4):
        print(" |      (_)   ")
        print(" |      \|/   ")
        print(" |            ")
        print(" |            ")

    if(erros == 5):
        print(" |      (_)   ")
        print(" |      \|/   ")
        print(" |       |    ")
        print(" |            ")

    if(erros == 6):
        print(" |      (_)   ")
        print(" |      \|/   ")
        print(" |       |    ")
        print(" |      /     ")

    if (erros == 7):
        print(" |      (_)   ")
        print(" |      \|/   ")
        print(" |       |    ")
        print(" |      / \   ")

    print(" |            ")
    print("_|___         ")
    print()

if(__name__== "__main__"):
    jogar()

