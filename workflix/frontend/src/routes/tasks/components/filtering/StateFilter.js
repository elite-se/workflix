// @flow

import React from 'react'
import type { FiltersType } from '../../types/Filters'
import { Button, Icon, Menu, MenuItem, Popover } from '@blueprintjs/core'
import { Position } from '@blueprintjs/core/lib/cjs/common/position'
import styled from 'styled-components'
import type { ProcessStatusType } from '../../../../modules/datatypes/Process'
import { difference, isEmpty } from 'lodash'

type PropsType = {|
  filters: FiltersType,
  onFiltersChanged: (FiltersType) => void
|}

type StatusRepresentationType = {|
  label: string,
  icon: string
|}

const IconWithMargin = styled(Icon)`
    margin: 0 2px 2px 0;
`

const statusRepresentation: Map<ProcessStatusType, StatusRepresentationType> =
  new Map<ProcessStatusType, StatusRepresentationType>([
    ['RUNNING', {
      label: 'running',
      icon: 'circle'
    }],
    ['ABORTED', {
      label: 'aborted',
      icon: 'delete'
    }],
    ['CLOSED', {
      label: 'done',
      icon: 'confirm'
    }]
  ])
const allProcessStatuses = Array.from(statusRepresentation.keys())

class StateFilter extends React.Component<PropsType> {
  getButtonText (): React$Node {
    const selectedStatuses = this.props.filters.status || allProcessStatuses
    const selectedStatusesDrawn = isEmpty(selectedStatuses) ? allProcessStatuses : selectedStatuses
    return <div style={{ textAlign: 'center' }}>
      <span style={{ marginRight: '5px' }}>Status:</span>
      {allProcessStatuses.map(status =>
        selectedStatusesDrawn.includes(status) && <IconWithMargin icon={statusRepresentation.get(status)?.icon}/>
      )}
    </div>
  }

  getPopoverContent (): React$Node {
    const selectedStatuses = this.props.filters.status || []
    return <Menu>{
      allProcessStatuses.map(status => {
        const repr = statusRepresentation.get(status)
        return repr && <MenuItem key={status}
                                 text={repr.label}
                                 labelElement={<Icon icon={repr.icon}/>}
                                 icon={selectedStatuses.includes(status) ? 'tick' : ''}
                                 shouldDismissPopover={false}
                                 onClick={this.onToggleStatus(status)}
        />
      })
    }
    </Menu>
  }

  onToggleStatus = (status: ProcessStatusType) => () => {
    const selectedStatuses = this.props.filters.status || []
    const updatedStatuses = selectedStatuses.includes(status)
      ? difference(selectedStatuses, [status])
      : [...selectedStatuses, status]
    this.props.onFiltersChanged({
      ...this.props.filters,
      status: updatedStatuses
    })
  }

  render () {
    return <Popover
      content={this.getPopoverContent()}
      position={Position.BOTTOM_LEFT}>
      <Button
        rightIcon='caret-down'
        text={this.getButtonText()}
      />
    </Popover>
  }
}

export default StateFilter
