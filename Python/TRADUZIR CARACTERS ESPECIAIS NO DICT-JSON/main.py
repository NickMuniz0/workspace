import re
import json


print(hex(ord('\u0014')))

def traduzir_caracter(texto):
    char_to_replace={
                        ord(']'):'!',
                        ord('^'):'¬',
                        ord('!'):'|',
                        ord('¢'):'^',
                        ord('¬'):'[',
                        ord('|'):']'
                    }
    texto=texto.translate(char_to_replace)
    return texto

def remove_unicode(texto):
    return re.sub(r'([\\]+[u]+[00]+([0-9]|[A-Z]){2})|([0]+[x]+([0-9]){2})','',texto) # Remove Unicode

def formatar(texto):
    dados = json.loads(texto)
    dados['body']['metadado']['codigo_comprovante']="\u0014"
    for key,values in dados['body']['metadado'].items():
        dados['body']['metadado'][key]=remove_unicode(traduzir_caracter(values))

    for obj_variaveis in dados['body']['dados_variaveis']:
        for key,values in obj_variaveis.items():
            obj_variaveis[key]=remove_unicode(traduzir_caracter(values))
    return dados

texto = '{"body":{"metadado":{"codigo_comprovante":"","versao":"![|]"},"dados_fixos":"","dados_variaveis":[{"TEXTE":""},{"TEXTE1":"OLA2"}]}}'

resultado = formatar(texto)
print(resultado)


#(:+".*?")+ # REGEX VALORES DE UM JSON EM STRING