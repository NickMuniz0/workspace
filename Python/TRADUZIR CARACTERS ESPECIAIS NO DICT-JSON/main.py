texto = '{"body":{"dados_fixos":{"T1":{"C1":"^"},"T2":"¢","T3":{"C1":{"D1":"!","D2":"¬"}}},"dados_variaveis":[{"Teste1":"!^"},{"Teste2":"^"}]}}'
texto_antigo = '{"body":{"dados_fixos":{"T1":{"C1":"^"},"T2":"¢","T3":{"C1":{"D1":"!","D2":"¬"}}},"dados_variaveis":[{"Teste1":"!^"},{"Teste2":"^"}]}}'
char_to_replace={
                    ord(']'):'!',
                    ord('^'):'¬',
                    ord('!'):'|',
                    ord('¢'):'^',
                    ord('¬'):'[',
                    ord('|'):']'
                }
ultimo= texto[-3:]
texto=texto.translate(char_to_replace)
texto= texto.replace(texto[-3:],ultimo).replace(':¢',':[')
print(texto)
print(texto_antigo)
# print(hex(ord('[')))