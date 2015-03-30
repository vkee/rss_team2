# quickly add aliases -----------------------------------
al() { echo 'alias '$1"='"${*:2}"'" >> ~/RSS-I-group/.bash_aliases; }

# lab launch -------------------------------------------
alias l6GUI='roslaunch lab6 mapGUI.launch'
alias l6nav='roslaunch lab6 nav.launch'
alias l5nav='roslaunch lab5 nav.launch'
alias l5gui='roslaunch lab5 sonarGUI.launch'
alias l7p4='l7; roslaunch lab7 part4.launch'
#alias bpu='source ~/.bashrc'

# lab folders -------------------------------------------
alias l5='cd ~/RSS-I-group/lab5'
alias l6='cd ~/RSS-I-group/lab6'
alias l7='cd ~/RSS-I-group/lab7'
alias l7d='cd /home/rss-student/RSS-I-group/lab7/docs'

# bashing ----------------------------------------------
alias bpu='source ~/.bashrc'
alias ba='nano ~/RSS-I-group/.bash_aliases'

# git --------------------------------------------------
alias gd='git pull'
alias gp='git push'
alias ga='git add'
alias gc='git commit -m'
alias gl='git log --pretty=format:"%h %ad%x09%an%x09%s" --date=short' # 2015-03-28
alias gs='git status' #this overwrites gs for launching ghostscript



