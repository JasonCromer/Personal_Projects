# R function to graph a dotplot
# This is inefficient code, but is fine for small examples
#
# Written by Bret Larget, September 3, 2003
#



  
dotplot = function(x,tol=1e-08) {
  y = rep(1,length(x))
  sx = sort(x)
  dx = diff(sx)
  maxHt = 1
  ht = 1
  for(i in 2:length(sx)) {
    if(abs(dx[i-1])<tol)
      ht = ht+0.1
    else
      ht = 1
    if(ht > maxHt)
      maxHt = ht
  }
  
  plot(x,y,type="n",axes=F,xlab="",ylab="",ylim=c(1,maxHt+1))
  axis(1,pretty(x))
  points(sx[1],1,pch=16)
  ht = 1
  for(i in 2:length(sx)) {
    if(abs(dx[i-1])<tol)
      ht = ht+0.1
    else
      ht = 1
    points(sx[i],ht,pch=16)
  }
  invisible(NULL)
}
