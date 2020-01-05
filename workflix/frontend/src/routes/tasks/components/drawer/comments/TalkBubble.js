// @flow

import styled from 'styled-components'

export const TalkBubble = styled<{ floatEnd: boolean }, {}, 'div'>('div')`
    margin: 0 10px 10px;
    display: block;
    position: relative;
    float: inline-${props => props.floatEnd ? 'end' : 'start'};
    width: 85%;
    height: auto;
    padding: 5px;
    border: 1px solid #666;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    
    @keyframes pop-up {
      0% {
       -webkit-transform: scale(0);
        -ms-transform: scale(0);
        transform: scale(0); 
      }
      100% {
        -webkit-transform: scale(1);
        -ms-transform: scale(1);
        transform: scale(1); 
      }
    }
    animation-duration: 0.3s;
`
