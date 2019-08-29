
## Git指南
    1、Git是分布式版本管理工具，最初由Linus用C写成来管理Linux源码，其他竞争对手有：BitKeeper、Mercurial、Bazaar
       集中式管理系统有：CVS、SVN（比CVS更稳定）、IBM的ClearCase、微软的VSS
       
       git管理的是修改而不是文件本身（二进制文件除外），类似MySQl中binlog的statement和row模式，而SVN是管理的文件，直接存储不同版本的文件
       
    2、安装后添加全局配置：$ git config --global user.name "Your Name"
                        $ git config --global user.email "email@example.com"
                        
    3、创建版本库repository：git init 会把当前目录创建为一个git仓库
    
    4、添加文件两个步骤add和commit： git add README.md  可以添加多个文件后再一起commit：git commit -m "add a readme file"
       删除文件 git rm <file> 然后再commit

    5、git status 可以用来查看仓库的当前状态（修改了哪些文件）
       git diff 可以用来查看修改了文本文件的哪些内容
       git log 倒序查看提交的历史记录，可以加参数 git log --pretty=oneline 来更友好的单行显示
       git reflog 查看每次操作的命令
       
    6、git的commit id（版本号）与SVN的不一样，不是递增的，而是SHA1的值的十六进制表示（这也是分布式的体现，如果要递增那么版本号就需要统一分配）
    
    7、head表示当前版本，head^表示上一个版本，head~50表示前第50个版本
       撤销修改：git checkout --<file> 把<file>在工作区的修改全部撤销，回滚到最近一次add（如果有）或者commit的状态
                                      注意参数中的--
       版本回退：git reset --hard HEAD^ 可以回退到上一个版本， git reset HEAD <file> 可以把暂存区某文件回退到当前版本
       
    8、工作区（.git的父目录），版本库Repository即.git目录，暂存区stage，
       工作区的修改add后会放到stage中，stage中的内容在commit后会移到HEAD（当前分支）中
       
    9、