package no.stelar7.api.l4j8.pojo.staticdata.champion;

public class Info
{
    private Integer attack;
    private Integer defense;
    private Integer difficulty;
    private Integer magic;
    
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        final Info other = (Info) obj;
        if (this.attack == null)
        {
            if (other.attack != null)
            {
                return false;
            }
        } else if (!this.attack.equals(other.attack))
        {
            return false;
        }
        if (this.defense == null)
        {
            if (other.defense != null)
            {
                return false;
            }
        } else if (!this.defense.equals(other.defense))
        {
            return false;
        }
        if (this.difficulty == null)
        {
            if (other.difficulty != null)
            {
                return false;
            }
        } else if (!this.difficulty.equals(other.difficulty))
        {
            return false;
        }
        if (this.magic == null)
        {
            if (other.magic != null)
            {
                return false;
            }
        } else if (!this.magic.equals(other.magic))
        {
            return false;
        }
        return true;
    }
    
    /**
     * Gets the attack.
     *
     * @return the attack
     */
    public Integer getAttack()
    {
        return this.attack;
    }
    
    /**
     * Gets the defense.
     *
     * @return the defense
     */
    public Integer getDefense()
    {
        return this.defense;
    }
    
    /**
     * Gets the difficulty.
     *
     * @return the difficulty
     */
    public Integer getDifficulty()
    {
        return this.difficulty;
    }
    
    /**
     * Gets the magic.
     *
     * @return the magic
     */
    public Integer getMagic()
    {
        return this.magic;
    }
    
    @Override
    public int hashCode()
    {
        final int prime  = 31;
        int       result = 1;
        result = (prime * result) + ((this.attack == null) ? 0 : this.attack.hashCode());
        result = (prime * result) + ((this.defense == null) ? 0 : this.defense.hashCode());
        result = (prime * result) + ((this.difficulty == null) ? 0 : this.difficulty.hashCode());
        result = (prime * result) + ((this.magic == null) ? 0 : this.magic.hashCode());
        return result;
    }
    
    @Override
    public String toString()
    {
        return "Info{" +
               "attack=" + attack +
               ", defense=" + defense +
               ", difficulty=" + difficulty +
               ", magic=" + magic +
               '}';
    }
}
