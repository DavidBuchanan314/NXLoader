![logo](https://i.imgur.com/o335KYo.png "logo")

# NXLoader
Meu primeiro aplicativo Android: Lançamento de cargas Fusée Gelée de ações Android

Heavily based on [Fusée Gelée](https://github.com/reswitched/fusee-launcher/) and [ShofEL2](https://github.com/fail0verflow/shofel2). [fusee.bin](https://github.com/ktemkin/Atmosphere/tree/poc_nvidia/fusee) is bundled as a default payload

## Nota: Todas as cargas proprietárias não são testadas nem suportadas por este software.

## Funciona no seu dispositivo? [Reportar aqui] (https://github.com/DavidBuchanan314/NXLoader/issues/1)
## [Obter o lançamento do APK] (https://github.com/DavidBuchanan314/NXLoader/releases)

Este aplicativo está atualmente no estado "Alpha", é o meu primeiro aplicativo Android e lá
é algum código bastante repugnante (Potencialmente bloqueando tarefas no thread da interface do usuário). Isso será melhorado em breve ™.

## COMO:
- Inicie o aplicativo.
- (Opcional) vá para a guia Config e selecione um arquivo de carga útil personalizado.
- Conecte seu Switch. (No meu Nexus 5, eu uso um cabo micro USB OTG e um cabo A-para-C)
- Coloque no modo RCM. (Nota: o seu comutador ligará sozinho quando conectado, segure VOL +).
- Conceda permissão ao aplicativo para acessar o dispositivo USB.
- Apreciar!

Nota: o aplicativo não precisa estar em execução para ativar a carga útil. O telefone pode até ser bloqueado!

## PERGUNTAS FREQUENTES:
- Por que usar isso em um lançador baseado na Web ?: Não é necessário usar a Internet e pode iniciar automaticamente mesmo que o telefone esteja bloqueado. Plug and play!
- Pode carregar o Linux ?: soon ™
- Será que ele tijolo meu telefone / switch ?: Esperemos que não, mas eu certamente não é responsável se isso acontecer!
- Precisa de raiz ?: Não!

## FAÇAM:
Refatorar o código para que seja menos hacky
- Melhorar a interface do usuário / UX
- Carregador de implementos para o Linux do fail0verflow

Para quem quer olhar para a fonte de exploração, a mágica acontece [aqui] (https://github.com/DavidBuchanan314/NXLoader/blob/master/app/src/main/java/io/github/davidbuchanan314/nxloader/ PrimaryLoader.java).
