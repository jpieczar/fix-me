let SessionLoad = 1
if &cp | set nocp | endif
let s:so_save = &so | let s:siso_save = &siso | set so=0 siso=0
let v:this_session=expand("<sfile>:p")
silent only
silent tabonly
cd ~/Desktop/my_fixme
if expand('%') == '' && !&modified && line('$') <= 1 && getline(1) == ''
  let s:wipebuf = bufnr('%')
endif
set shortmess=aoO
argglobal
%argdel
$argadd router/
$argadd broker/
$argadd market/
$argadd core/
tabnew
tabnew
tabnew
tabrewind
edit router/src/main/java/com/wtc/App.java
set splitbelow splitright
set nosplitbelow
set nosplitright
wincmd t
set winminheight=0
set winheight=1
set winminwidth=0
set winwidth=1
argglobal
if bufexists("router/src/main/java/com/wtc/App.java") | buffer router/src/main/java/com/wtc/App.java | else | edit router/src/main/java/com/wtc/App.java | endif
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let s:l = 24 - ((20 * winheight(0) + 17) / 34)
if s:l < 1 | let s:l = 1 | endif
exe s:l
normal! zt
24
normal! 09|
tabnext
edit broker/src/main/java/com/wtc/App.java
set splitbelow splitright
set nosplitbelow
set nosplitright
wincmd t
set winminheight=0
set winheight=1
set winminwidth=0
set winwidth=1
argglobal
if bufexists("broker/src/main/java/com/wtc/App.java") | buffer broker/src/main/java/com/wtc/App.java | else | edit broker/src/main/java/com/wtc/App.java | endif
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let s:l = 1 - ((0 * winheight(0) + 17) / 34)
if s:l < 1 | let s:l = 1 | endif
exe s:l
normal! zt
1
normal! 0
tabnext
edit market/src/main/java/com/wtc/App.java
set splitbelow splitright
set nosplitbelow
set nosplitright
wincmd t
set winminheight=0
set winheight=1
set winminwidth=0
set winwidth=1
argglobal
if bufexists("market/src/main/java/com/wtc/App.java") | buffer market/src/main/java/com/wtc/App.java | else | edit market/src/main/java/com/wtc/App.java | endif
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let s:l = 5 - ((4 * winheight(0) + 17) / 34)
if s:l < 1 | let s:l = 1 | endif
exe s:l
normal! zt
5
normal! 0
tabnext
edit core/src/main/java/com/wtc/fixprotocol/Creator.java
set splitbelow splitright
set nosplitbelow
set nosplitright
wincmd t
set winminheight=0
set winheight=1
set winminwidth=0
set winwidth=1
argglobal
if bufexists("core/src/main/java/com/wtc/fixprotocol/Creator.java") | buffer core/src/main/java/com/wtc/fixprotocol/Creator.java | else | edit core/src/main/java/com/wtc/fixprotocol/Creator.java | endif
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let s:l = 37 - ((10 * winheight(0) + 17) / 34)
if s:l < 1 | let s:l = 1 | endif
exe s:l
normal! zt
37
normal! 063|
tabnext 4
badd +0 router/
badd +0 broker/
badd +0 market/
badd +0 core/
badd +1 broker/src/main/java/com/wtc/App.java
badd +1 market/src/main/java/com/wtc/App.java
badd +43 core/src/main/java/com/wtc/fixprotocol/Creator.java
badd +1 router/src/main/java/com/wtc/App.java
badd +20 core/src/main/java/com/wtc/utils/Tags.java
badd +15 core/src/main/java/com/wtc/App.java
if exists('s:wipebuf') && len(win_findbuf(s:wipebuf)) == 0
  silent exe 'bwipe ' . s:wipebuf
endif
unlet! s:wipebuf
set winheight=1 winwidth=20 shortmess=filnxtToOSI
set winminheight=1 winminwidth=1
let s:sx = expand("<sfile>:p:r")."x.vim"
if filereadable(s:sx)
  exe "source " . fnameescape(s:sx)
endif
let &so = s:so_save | let &siso = s:siso_save
nohlsearch
doautoall SessionLoadPost
unlet SessionLoad
" vim: set ft=vim :
