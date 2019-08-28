
## Git指南
    1、Git是分布式版本管理工具，最初由Linus用C写成来管理Linux源码，其他竞争对手有：BitKeeper、Mercurial、Bazaar
       集中式管理系统有：CVS、SVN（比CVS更稳定）、IBM的ClearCase、微软的VSS
       
    2、安装后添加全局配置：$ git config --global user.name "Your Name"
                        $ git config --global user.email "email@example.com"
                        
    3、创建版本库repository：git init 会把当前目录创建为一个git仓库
    
    4、添加文件：git add README.md  可以添加多个文件后再一起commit：git commit -m "add a readme file"
    
    5、git status 可以用来查看仓库的当前状态（修改了哪些文件）
       git diff 可以用来查看修改了文本文件的哪些内容
       git log 倒序查看提交的历史记录