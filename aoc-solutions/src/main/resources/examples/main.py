F,S,p=[],[],0
for i, c in enumerate(open('example_09.txt').read().strip()):
    [F,S][i%2] += [[*range(p,p:=p+int(c))]]
for y in reversed(range(len(F))):
    for x in range(len(S)):
        if len(S[x]) >= len(F[y]) and F[y][0] > S[x][0]:
            F[y] = S[x][:len(F[y])]
            S[x] = S[x][len(F[y]):]
print(sum((i*j) for i,f in enumerate(F) for j in f))

# 6636608781232

# 6675431665828

# java 10000 847321986203
# python 10000 836976396530

# python 201 9393427
# java 201 9551440

# python 150 2930797
# java 150 2930797

# python 180 6232771
# java 180 6232771

# python 190 7898423
# java 190 8123176

# python 100 1063723
# java 100 1063723


# python 100-200 1118171
# java 100-200 1118171
