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
`
